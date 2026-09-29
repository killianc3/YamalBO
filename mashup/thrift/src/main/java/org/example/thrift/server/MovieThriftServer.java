package org.example.thrift.server;

import movie.MovieModel;
import movie.MovieModelFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.example.thrift.gen.MovieService;
import org.example.thrift.service.MovieServiceThriftImpl;

public final class MovieThriftServer {

    public static final int DEFAULT_PORT = 9090;

    private MovieThriftServer() {
    }

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        MovieModel model = MovieModelFactory.getModel();
        MovieServiceThriftImpl handler = new MovieServiceThriftImpl(model);
        MovieService.Processor<MovieServiceThriftImpl> processor = new MovieService.Processor<>(handler);

        TServerTransport transport = new TServerSocket(port);
        TServer server = new TSimpleServer(new TServer.Args(transport).processor(processor));

        System.out.println("MovieService (Thrift) en ecoute sur le port " + port);
        server.serve();
    }
}
