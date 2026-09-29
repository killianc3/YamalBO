namespace java org.example.thrift.gen

struct MovieDto {
  1: required string title,
  2: required i16 year,
  3: required string visualisationDate,
  4: required i16 points
}

exception ServiceMovieNotFoundException {
  1: required string message
}

service MovieService {

  void addMovie(1: MovieDto movie),

  MovieDto findMovieByTitle(1: string title) throws (1: ServiceMovieNotFoundException notFound),

  list<MovieDto> findMoviesByYear(1: i16 year)
}
