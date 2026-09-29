package org.example.thrift.server;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.example.thrift.gen.MovieDto;
import org.example.thrift.gen.MovieService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieThriftServerIT {

    private static final int PORT = 9099;
    private static Thread serverThread;

    @BeforeAll
    static void startServer() throws Exception {
        serverThread = new Thread(() -> {
            try {
                MovieThriftServer.main(new String[]{String.valueOf(PORT)});
            } catch (Exception ignored) {
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(500);
    }

    @AfterAll
    static void stopServer() {
        serverThread.interrupt();
    }

    @Test
    void addThenFind_overRealThriftConnection() throws Exception {
        TTransport transport = new TSocket("localhost", PORT);
        transport.open();
        try {
            TProtocol protocol = new TBinaryProtocol(transport);
            MovieService.Client client = new MovieService.Client(protocol);

            client.addMovie(new MovieDto("Interstellar", (short) 2014, "2025-03-01", (short) 10));
            MovieDto found = client.findMovieByTitle("Interstellar");

            assertEquals("Interstellar", found.getTitle());
            assertEquals(2014, found.getYear());
            assertEquals("2025-03-01", found.getVisualisationDate());
            assertEquals(10, found.getPoints());
        } finally {
            transport.close();
        }
    }
}
