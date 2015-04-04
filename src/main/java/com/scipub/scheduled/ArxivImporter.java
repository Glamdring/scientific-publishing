package com.scipub.scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.scipub.dao.jpa.BranchDao;
import com.scipub.model.Branch;
import com.scipub.model.Language;
import com.scipub.model.PaperStatus;
import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationSource;
import com.scipub.service.PublicationService;


@Component
public class ArxivImporter {

    private static final Logger logger = LoggerFactory.getLogger(ArxivImporter.class);
    
    private static final DateTimeFormatter DATE_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(TimeZone.getTimeZone("UTC").toZoneId());
    
    private static final String ROOT_RSS_URL = "http://export.arxiv.org/rss/";
    private static final String URI_PREFIX = "http://arxiv.org/abs/";
    
    private static final Multimap<String, String> BRANCHES = HashMultimap.create();
    
    private static final String[] FEEDS = new String[] {
        "astro-ph", "cond-mat", "gr-qc", "hep-ex", "hep-lat", "hep-ph", "hep-th", "math-ph", "nlin",
        "nucl-ex", "nucl-th", "physics", "quant-ph",
        "math",
        "cs",
        "q-bio",
        "q-fin",
        "stat"
    };
    
    static {
        BRANCHES.put("astro-ph.GA", "Astrophysics of Galaxies"); //?
        BRANCHES.put("astro-ph.CO", "Physical cosmology"); //?
        BRANCHES.put("astro-ph.EP", "Planetary science"); //?
        BRANCHES.put("astro-ph.HE", "High energy astrophysics");
        BRANCHES.put("astro-ph.IM", "Methods for astrophysics");
        BRANCHES.put("astro-ph.SR", "Astrophysics");
        
        BRANCHES.put("cond-mat.dis-nn", "Disordered systems");
        BRANCHES.put("cond-mat.mtrl-sci", "Materials physics");
        BRANCHES.put("cond-mat.mes-hall", "Mesoscale and nanoscale physics");
        BRANCHES.put("cond-mat.other", "Condensed matter physics");
        BRANCHES.put("cond-mat.quant-gas", "Quantum gases");
        BRANCHES.put("cond-mat.soft", "Soft condensed matter");
        BRANCHES.put("cond-mat.stat-mech", "Statistical mechanics");
        BRANCHES.put("cond-mat.str-el", "Strongly correlated electrons");
        BRANCHES.put("cond-mat.supr-con", "Superconductivity");
        
        BRANCHES.put("gr-qc", "Relativity");
        BRANCHES.put("gr-qc", "Quantum cosmology");
        
        BRANCHES.put("hep-ex", "Experimental high energy physics");
        BRANCHES.put("hep-lat", "Lattice high energy physics");
        BRANCHES.put("hep-ph", "Phenomenology of high energy physics");
        BRANCHES.put("hep-th", "Theoretical high energy physics");
        
        BRANCHES.put("math-ph", "Mathematical physics");
        
        BRANCHES.put("nlin.AO", "Adaptation and Self-Organizing Systems");
        BRANCHES.put("nlin.CG", "Cellular Automata and Lattice Gases");
        BRANCHES.put("nlin.CD", "Chaotic Dynamics");
        BRANCHES.put("nlin.SI", "Exactly Solvable and Integrable Systems");
        BRANCHES.put("nlin.PS", "Pattern Formation and Solitons");
        
        BRANCHES.put("nucl-ex", "Experimental nuclear physics");
        BRANCHES.put("nucl-th", "Theoretical nuclear physics");
        
        BRANCHES.put("physics.acc-ph", "Accelerator physics");
        BRANCHES.put("physics.ao-ph", "Atmospheric physics");
        BRANCHES.put("physics.atom-ph", "Atomic physics");
        BRANCHES.put("physics.atm-clus", "Atomic, molecular, and optical physics");
        BRANCHES.put("physics.bio-ph", "Biophysics");
        BRANCHES.put("physics.chem-ph", "Chemical physics");
        BRANCHES.put("physics.class-ph", "Classical mechanics");
        BRANCHES.put("physics.comp-ph", "Computational physics");
        BRANCHES.put("physics.data-an", "Statistical physics");
        BRANCHES.put("physics.flu-dyn", "Fluid dynamics");
        BRANCHES.put("physics.gen-ph", "Physics");
        BRANCHES.put("physics.geo-ph", "Geophysics");
        BRANCHES.put("physics.hist-ph", "History of physics");
        BRANCHES.put("physics.hist-ph", "Philosophy of physics");
        BRANCHES.put("physics.ins-det", "Instrumentation and detectors (physics)");
        BRANCHES.put("physics.med-ph", "Medical physics");
        BRANCHES.put("physics.optics", "Optics");
        BRANCHES.put("physics.ed-ph", "Science education");
        BRANCHES.put("physics.soc-ph", "Physics and society");
        BRANCHES.put("physics.plasm-ph", "Plasma physics");
        BRANCHES.put("physics.space-ph", "Space plasma physics");
        
        BRANCHES.put("quant-ph", "Quantum physics");
        
        // end of Physics
        
        BRANCHES.put("math.AG", "Algebraic geometry");
        BRANCHES.put("math.AT", "Algebraic topology");
        BRANCHES.put("math.AP", "Partial differential equations");
        BRANCHES.put("math.CT", "Category theory");
        BRANCHES.put("math.CA", "Ordinary differential equations");
        BRANCHES.put("math.CO", "Combinatorics");
        BRANCHES.put("math.AC", "Commutative algebra");
        BRANCHES.put("math.CV", "Complex analysis");
        BRANCHES.put("math.DG", "Differential geometry");
        BRANCHES.put("math.DS", "Dynamical systems");
        BRANCHES.put("math.FA", "Functional analysis");
        BRANCHES.put("math.GM", "Mathematics");
        BRANCHES.put("math.GN", "General topology");
        BRANCHES.put("math.GT", "Geometric topology");
        BRANCHES.put("math.GR", "Group theory");
        BRANCHES.put("math.HO", "History of mathematics");
        BRANCHES.put("math.IT", "Information theory");
        BRANCHES.put("math.KT", "Homological algebra");
        BRANCHES.put("math.LO", "Mathematical logic");
        BRANCHES.put("math.MP", "Mathematical physics");
        BRANCHES.put("math.MG", "Metric geometry");
        BRANCHES.put("math.NT", "Number theory");
        BRANCHES.put("math.NA", "Numerical analysis");
        BRANCHES.put("math.OA", "Operator theory");
        BRANCHES.put("math.OC", "Optimization (mathematics)");
        BRANCHES.put("math.PR", "Probability theory");
        BRANCHES.put("math.QA", "Quantum algebra");
        BRANCHES.put("math.RT", "Group representation"); //?
        BRANCHES.put("math.RA", "Ring theory");
        BRANCHES.put("math.SP", "Algebra"); //?
        BRANCHES.put("math.ST", "Statistical theory");
        BRANCHES.put("math.SG", "Symplectic geometry");
        // end of mathematics
        
        BRANCHES.put("cs.AI", "Artificial intelligence");
        BRANCHES.put("cs.CL", "Computational linguistics");
        BRANCHES.put("cs.CC", "Computational complexity theory");
        BRANCHES.put("cs.CE", "Scientific computing");
        BRANCHES.put("cs.GT", "Game theory");
        BRANCHES.put("cs.CV", "Computer vision");
        BRANCHES.put("cs.CY", "Computational sociology");
        BRANCHES.put("cs.CR", "Computer security");
        BRANCHES.put("cs.CR", "Cryptography");
        BRANCHES.put("cs.DS", "Data structures");
        BRANCHES.put("cs.DS", "Algorithms");
        BRANCHES.put("cs.DB", "Database");
        BRANCHES.put("cs.DM", "Computer science");
        BRANCHES.put("cs.DC", "Distributed computing");
        BRANCHES.put("cs.DC", "Parallel computing");
        BRANCHES.put("cs.FL", "Automata theory");
        BRANCHES.put("cs.GR", "Computer graphics");
        BRANCHES.put("cs.AR", "Computer architecture");
        BRANCHES.put("cs.HC", "Human-computer interaction");
        BRANCHES.put("cs.IR", "Information retrieval");
        BRANCHES.put("cs.IT", "Information theory");
        BRANCHES.put("cs.LG", "Machine learning");
        BRANCHES.put("cs.MM", "Multimedia");
        BRANCHES.put("cs.NI", "Computer networking");
        BRANCHES.put("cs.NE", "Artificial neural networks");
        BRANCHES.put("cs.NA", "Numerical analysis");
        BRANCHES.put("cs.OS", "Operating systems");
        BRANCHES.put("cs.OH", "Computer science");
        BRANCHES.put("cs.PL", "Programming languages");
        BRANCHES.put("cs.RO", "Robotics");
        BRANCHES.put("cs.SE", "Software engineering");
        BRANCHES.put("cs.SC", "Symbolic computation");
        // End of computer science
        
        BRANCHES.put("q-bio.CB", "Cell biology");
        BRANCHES.put("q-bio.GN", "Genomics");
        BRANCHES.put("q-bio.NC", "Neurobiology");
        BRANCHES.put("q-bio.PE", "Population dynamics");
        // End of quantitative biology
        
        BRANCHES.put("q-fin.CP", "Computational finance");
        BRANCHES.put("q-fin.EC", "Economics");
        BRANCHES.put("q-fin.GN", "Finance");
        BRANCHES.put("q-fin.MF", "Mathematical finance");
        BRANCHES.put("q-fin.RM", "Risk management");
        // End of quantitative finance
        
        BRANCHES.put("stat.CO", "Computational statistics");
        BRANCHES.put("stat.OT", "Statistics");
        BRANCHES.put("stat.ST", "Statistical theory");
        BRANCHES.put("stat.ML", "Machine learning");
        // End of statistics
        
    }
    
    private Joiner joiner = Joiner.on(',');
    
    @Inject
    private BranchDao dao;
    
    @Inject
    private PublicationService service;
    
    @Scheduled(fixedRate=DateTimeConstants.MILLIS_PER_DAY)
    public void run() {
        LocalDateTime lastImport = service.getLastImportDate(PublicationSource.ARXIV);
        for (String feedKey : FEEDS) {
            SyndFeedInput input = new SyndFeedInput();
            try {
                SyndFeed feed = input.build(new XmlReader(new URL(ROOT_RSS_URL + feedKey)));
                List<String> ids = new ArrayList<String>(feed.getEntries().size());
                for (SyndEntry entry : feed.getEntries()) {
                    ids.add(entry.getLink().replace(URI_PREFIX, ""));
                }
                
                URL url = new URL("http://export.arxiv.org/api/query?id_list=" + joiner.join(ids) + "&max_results=100");
                try (InputStream inputStream = url.openStream()) {
                    List<Publication> publications = extractPublications(inputStream, lastImport, ids.size());
                    publications = publications.stream().filter(p -> p.getCreated().isAfter(lastImport)).collect(Collectors.toList());
                    logger.info("Obtained publications from arxiv for category {}: {}", feedKey, publications.size());
                    service.storePublication(publications);
                }
                
            } catch (IllegalArgumentException | FeedException | IOException e) {
                logger.error("Problem getting arxiv RSS feed", e);
            }
        }
    }

    @VisibleForTesting
    List<Publication> extractPublications(InputStream inputStream, LocalDateTime lastImport, int total) throws FactoryConfigurationError {
        final List<Publication> publications = new ArrayList<>(total);
        final  SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(inputStream, new ArxivSaxHandler(new PublicationCallbackHandler() {
                @Override
                public void onNewPublication(Publication publication) {
                    publications.add(publication);
                }
            }, dao, lastImport));
        } catch (StopParsing ex) {
            logger.info("Stopped parsing because entries were before the last import date");
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new IllegalStateException("Failed to parse input", e);
        }
        
        return publications;
    }
    
    
    private static class ArxivSaxHandler extends DefaultHandler {
        private final PublicationCallbackHandler callbackHandler;
        private LocalDateTime lastImport;
        
        private Publication currentPublication;
        private StringBuilder currentValue;
        
        private Attributes currentAttributes;

        private BranchDao dao;
        
        public ArxivSaxHandler(PublicationCallbackHandler callbackHandler, BranchDao dao, LocalDateTime lastImport) {
            this.callbackHandler = callbackHandler;
            this.dao = dao;
            this.lastImport = lastImport;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // we are not checking for segments and instead flatten the contents of a track
            if (localName.equals("entry")) {
                currentPublication = new Publication();
                currentPublication.setCurrentRevision(new PublicationRevision());
                currentPublication.getCurrentRevision().setPublication(currentPublication);
                currentPublication.setSource(PublicationSource.ARXIV);
                currentPublication.setLanguage(Language.EN);
                currentPublication.setStatus(PaperStatus.PUBLISHED);
            }
            currentAttributes = attributes;
            
            currentValue = new StringBuilder();
        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (currentValue != null) {
                // supporting multiple "characters" event per text node
                String value = new String(ch, start, length);
                currentValue.append(value);
            }
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            try {
                setProperties(localName);
                currentValue = null;
                if (localName.equals("entry")) {
                    // do not process publications for which we could not find a corresponding branch
                    if (currentPublication != null && currentPublication.getPrimaryBranch() != null) {

                        // set parent branches of all the detected branches
                        for (Branch branch : currentPublication.getBranches()) {
                            while (branch.getParentBranch() != null) {
                                branch = branch.getParentBranch();
                                currentPublication.getParentBranches().add(branch);
                            }
                        }
                        callbackHandler.onNewPublication(currentPublication);
                    }
                    currentPublication = null;
                }
            } catch (StopParsing stop) {
                throw stop;
            } catch (Exception ex) {
                logger.warn("Problem parsing element " + localName + ". Publication is: " + currentPublication, ex);
            }
        }

        private void setProperties(String currentElement) throws StopParsing {
            if (currentValue == null) {
                return;
            }
            String value = currentValue.toString();
            if (currentPublication != null) {
                if (currentElement.equals("name")) {
                    currentPublication.getNonRegisteredAuthors().add(value);
                } else if (currentElement.equals("id")) {
                    currentPublication.setUri(value);
                } else if (currentElement.equals("link")) {
                    if (currentAttributes == null) {
                        throw new IllegalStateException("currentAttributes is null, " + value);
                    }
                    String type = currentAttributes.getValue(currentAttributes.getIndex("type"));
                    if (type != null && type.equals("application/pdf")) {
                        currentPublication.getCurrentRevision().setContentLink(
                                currentAttributes.getValue(currentAttributes.getIndex("href")));
                    }
                } else if (currentElement.equals("doi")) {
                    currentPublication.setDoi(value);
                } else if (currentElement.equals("summary")) {
                    currentPublication.getCurrentRevision().setPublicationAbstract(value);
                } else if (currentElement.equals("published")) {
                    currentPublication.setCreated(DATE_FORMAT.parse(value, LocalDateTime::from));
                    if (currentPublication.getCreated().isBefore(lastImport)) {
                        throw new StopParsing();
                    }
                } else if (currentElement.equals("updated")) {
                    currentPublication.getCurrentRevision().setCreated(DATE_FORMAT.parse(value, LocalDateTime::from));
                } else if (currentElement.equals("title")) {
                    // strip new lines and double-spaces from titles
                    currentPublication.getCurrentRevision().setTitle(value.replace("\n", "").replace("  ", " "));
                } else if (currentElement.equals("primary_category")){
                    String branchKey = currentAttributes.getValue(currentAttributes.getIndex("term"));
                    List<Branch> branches = getBranches(branchKey);
                    if (!branches.isEmpty()) {
                        currentPublication.setPrimaryBranch(branches.iterator().next());
                    }
                } else if (currentElement.equals("category")) {
                    String branchKey = currentAttributes.getValue(currentAttributes.getIndex("term"));
                    currentPublication.getBranches().addAll(getBranches(branchKey));
                }
            }
        }

        private List<Branch> getBranches(String key) {
            Collection<String> branchNames = BRANCHES.get(key);
            return branchNames
                    .stream()
                    .map(name -> dao.getByPropertyValue(Branch.class, "name", name))
                    .filter(branch -> branch != null)
                    .collect(Collectors.toList());
        }

        @Override
        public void error(final SAXParseException e) throws SAXException {
            throw e;
        }
    }

    /**
     * Implementations of this interface are notified of events occurring during XML parsing.
     */
    public interface PublicationCallbackHandler {
        /**
         * Whenever a new publication is discovered, this method is invoked
         * @param publication
         */
        void onNewPublication(Publication publication);
    }

    /**
     * Exception used to indicate that parsing should stop
     */
    public static class StopParsing extends SAXException {
    }
}
