package org.example.thrift.servlet;

import movie.MovieModelFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServlet;
import org.example.thrift.gen.MovieService;
import org.example.thrift.service.MovieServiceThriftImpl;

public class MovieThriftServlet extends TServlet {

    public MovieThriftServlet() {
        super(new MovieService.Processor<>(new MovieServiceThriftImpl(MovieModelFactory.getModel())),
                new TBinaryProtocol.Factory());
    }
}
