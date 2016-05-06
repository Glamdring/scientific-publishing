/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.service;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

import javax.annotation.PreDestroy;

import lbms.plugins.mldht.DHTConfiguration;
import lbms.plugins.mldht.kad.DHT;
import lbms.plugins.mldht.kad.DHT.DHTtype;
import lbms.plugins.mldht.kad.DHT.LogLevel;
import lbms.plugins.mldht.kad.DHTIndexingListener;
import lbms.plugins.mldht.kad.DHTLogger;
import lbms.plugins.mldht.kad.DHTStatus;
import lbms.plugins.mldht.kad.DHTStatusListener;
import lbms.plugins.mldht.kad.KBucketEntry;
import lbms.plugins.mldht.kad.Key;
import lbms.plugins.mldht.kad.Node.RoutingTableEntry;
import lbms.plugins.mldht.kad.PeerAddressDBItem;
import lbms.plugins.mldht.kad.RPCServerListener;
import lbms.plugins.mldht.kad.messages.GetPeersRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Service
public class DHTService {

    private static final Logger logger = LoggerFactory.getLogger(DHTService.class);
    private DHT dht;
    
    public void publish(byte[] content, String filename) {
        
    }
    
    //@PostConstruct TODO enable
    public void init() throws Exception {
        DHT.setLogLevel(LogLevel.Debug);

        initialize();
        dht = DHT.getDHT(DHTtype.IPV4_DHT);
        dht.addIndexingLinstener(new DHTIndexingListener() {
            @Override
            public List<PeerAddressDBItem> incomingPeersRequest(Key infoHash, InetAddress sourceAddress, Key nodeID) {
                logger.info("Incoming peer request: " + infoHash);
                return Collections.emptyList();
            }
        });
        
        dht.addStatusListener(new DHTStatusListener() {
            @Override
            public void statusChanged(DHTStatus newStatus, DHTStatus oldStatus) {
                logger.info("DHT Status changed: " + newStatus);
            }
        });
        
        dht.start(getConfiguration(), getServerListener());
        Thread.sleep(10000);

        logger.info("Servers: " + dht.getServers());
        
        InetSocketAddress peer = null;
        System.out.println(dht.getNode().getNumEntriesInRoutingTable());
        for (RoutingTableEntry entry : dht.getNode().getBuckets()) {
            for (KBucketEntry bucketEntry : entry.getBucket().getEntries()) {
                if (bucketEntry.getAddress() != null) {
                    peer = bucketEntry.getAddress();
                }
            }
        }

        dht.getRandomServer().ping(peer);

        // TODO how to get response? We are notified in the server listener that there is a response, but not what
        // it is

        Thread.sleep(10000);

        System.out.println("-----sending get peers request-----");
        GetPeersRequest request =
                new GetPeersRequest(new Key(hexStringToByteArray("64DD3835A88FE9BE6826B4B701AD35177ABD376C")));
        request.setID(dht.getRandomServer().getDerivedID());
        request.setDestination(peer);
        dht.getRandomServer().sendMessage(request);

        Thread.sleep(10000);
        System.out.println(dht.getRandomServer().getNumReceived());
        System.out.println(dht.getNode().getNumEntriesInRoutingTable());
    }

    @PreDestroy
    public void destroy() {
        dht.stop();
    }
    private static RPCServerListener getServerListener() {
        return new RPCServerListener() {
            @Override
            public void replyReceived(InetSocketAddress fromNode) {
                System.out.println("Reply received from node: " + fromNode);
            }
        };

    }

    private static DHTConfiguration getConfiguration() {
        return new DHTConfiguration() {

            @Override
            public boolean noRouterBootstrap() {
                return false;
            }

            @Override
            public boolean isPersistingID() {
                return true;
            }

            @Override
            public File getNodeCachePath() {
                return new File("/tmp/dht");
            }

            @Override
            public int getListeningPort() {
                return 5556;
            }

            @Override
            public boolean allowMultiHoming() {
                return false;
            }
        };
    }

    void initialize() {
        DHT.createDHTs();

        DHT.setLogger(new DHTLogger() {
            /*
             * (non-Javadoc)
             * 
             * @see lbms.plugins.mldht.kad.DHTLogger#log(java.lang.String)
             */
            public void log(String message) {
                logger.info(message);
            }

            /*
             * (non-Javadoc)
             * 
             * @see lbms.plugins.mldht.kad.DHTLogger#log(java.lang.Exception)
             */
            public void log(Throwable e) {
                logger.warn("DHT problem", e);
            }
        });
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
