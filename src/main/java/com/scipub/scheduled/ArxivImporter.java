package com.scipub.scheduled;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


public class ArxivImporter {

    private static final Multimap<String, String> FEEDS = HashMultimap.create();
    static {
        FEEDS.put("astro-ph.GA", "Astrophysics of Galaxies"); //?
        FEEDS.put("astrp-ph.CO", "Physical cosmology"); //?
        FEEDS.put("astrp-ph.EP", "Planetary science"); //?
        FEEDS.put("astrp-ph.HE", "High energy astrophysics");
        FEEDS.put("astrp-ph.IM", "Methods for astrophysics");
        FEEDS.put("astrp-ph.SR", "Solar physics");
        FEEDS.put("astrp-ph.SR", "Interstellar astrophysics");
        
        FEEDS.put("cond-mat.dis-nn", "Disordered systems");
        FEEDS.put("cond-mat.mtrl-sci", "Materials physics");
        FEEDS.put("cond-mat.mes-hall", "Mesoscale and nanoscale physics");
        FEEDS.put("cond-mat.other", "Condensed matter physics");
        FEEDS.put("cond-mat.quant-gas", "Quantum gases");
        FEEDS.put("cond-mat.soft", "Soft condensed matter");
        FEEDS.put("cond-mat.stat-mech", "Statistical mechanics");
        FEEDS.put("cond-mat.str-el", "Strongly correlated electrons");
        FEEDS.put("cond-mat.supr-con", "Superconductivity");
        
        FEEDS.put("gr-qc", "Relativity");
        FEEDS.put("gr-qc", "Quantum cosmology");
        
        FEEDS.put("hep-ex", "Experimental high energy physics");
        FEEDS.put("hep-lat", "Lattice high energy physics");
        FEEDS.put("hep-ph", "Phenomenology of high energy physics");
        FEEDS.put("hep-th", "Theoretical high energy physics");
        
        FEEDS.put("math-ph", "Mathematical physics");
        
    }
}
