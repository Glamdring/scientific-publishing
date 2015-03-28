package com.scipub.scheduled;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.scipub.model.Language;
import com.scipub.model.PaperStatus;
import com.scipub.model.Publication;
import com.scipub.model.PublicationSource;


public class ArxivImporter {

    private static final Logger logger = LoggerFactory.getLogger(ArxivImporter.class);
    
    private static final String ROOT_RSS_URL = "http://export.arxiv.org/rss/";
    private static final String URI_PREFIX = "http://arxiv.org/abs/";
    
    private static final Multimap<String, String> BRANCHES = HashMultimap.create();
    
    private static final String[] FEEDS = new String[] {
        "astro-ph", "cond-mat", "gr-qc", "gr-qc", "hep-ex", "hep-lat", "hep-ph", "hep-th", "math-ph", "nlin",
        "nucl-ex", "nucl-th", "physics", "quant-ph",
        "math",
        "cs"
    };
    
    static {
        BRANCHES.put("astro-ph.GA", "Astrophysics of Galaxies"); //?
        BRANCHES.put("astrp-ph.CO", "Physical cosmology"); //?
        BRANCHES.put("astrp-ph.EP", "Planetary science"); //?
        BRANCHES.put("astrp-ph.HE", "High energy astrophysics");
        BRANCHES.put("astrp-ph.IM", "Methods for astrophysics");
        BRANCHES.put("astrp-ph.SR", "Astrophysics");
        
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
        BRANCHES.put("physics.soc-ph", "Sociology of science");
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
    }
    
    private Joiner joiner = Joiner.on(',');
    
    private RestTemplate restTemplate = new RestTemplate();
    
    @Scheduled(fixedRate=DateTimeConstants.MILLIS_PER_DAY)
    public void run() {
        for (String feedKey : FEEDS) {
            SyndFeedInput input = new SyndFeedInput();
            try {
                SyndFeed feed = input.build(new XmlReader(new URL(ROOT_RSS_URL + feedKey)));
                List<String> uris = new ArrayList<String>(feed.getEntries().size());
                for (SyndEntry entry : feed.getEntries()) {
                    uris.add(entry.getLink());
                }
                
                //TODO parse properly. It's an atom feed, so either use https://github.com/DSpace/DSpace/blob/master/dspace-api/src/main/java/org/dspace/submit/lookup/ArXivService.java
                // or use S(t)AX
                restTemplate.getForObject("http://export.arxiv.org/api/query?id_list=" + joiner.join(uris), ArxivFeed.class);
                
                List<Publication> publications = new ArrayList<>(uris.size());
                for (String uri : uris) {
                    String id = uri.replace(URI_PREFIX, "");
                    
                    
                    String publicationAbstract = null; //entry.getDescription().getValue(); //TODO convert from latex to MD
                    String link = null; //entry.getLink();
                    Set<String> authors = null; //TODO parse authors
                    String[] branchNames = null; //TODO parse branch names from page
                    
                    Publication publication = new Publication();
                    publication.setSource(PublicationSource.ARXIV);
                    publication.setLanguage(Language.EN);
                    publication.setNonRegisteredAuthors(authors);
                    publication.setCreated(null);
                    publication.setStatus(PaperStatus.PUBLISHED);
                    publication.setUri(null); // get arxiv uri http://arxiv.org/abs/1503.07585
                }
            } catch (IllegalArgumentException | FeedException | IOException e) {
                logger.error("Problem getting arxiv RSS feed", e);
            }
        }
    }
    
    
    private static class ArxivFeed {
        private List<ArxivEntry> entries;
    }
    
    private static class ArxivEntry {
        private String id;
        private String updated;
        private String published;
        private String title;
        private String summary;
        private List<Author> authors;
        @XmlAttribute(name="term")
        private List<String> categories;
        
        @XmlAttribute(name="term")
        private List<String> link;
        
        
        @XmlAttribute(name="term")
        private List<String> pdfLink;
        
        @XmlElement(name="primary_category")
        private String primaryCategory;
    }
    
    static class Author {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
}
