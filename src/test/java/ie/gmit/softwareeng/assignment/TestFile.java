package ie.gmit.softwareeng.assignment;


import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestFile {
   @Mock
   private FilmRepository filmRepository;
   @Mock
   private OmdbClient omdbClient;
   @Mock
   private Film film;


    FilmService filmService;

    @Before
    public void init(){
        filmService = new FilmService(filmRepository, omdbClient);
    }
    @Test(expected = FilmNotFoundException.class)
    public void mockGetfilm(){
        Mockito.when(filmRepository.getFilm(1)).thenReturn(null);
        filmService.rentFilm(1);
    }
    @Test
    public void failedReturn(){

    }

    @Test(expected = FilmNotFoundException.class)
    public void filmRentNotFound(){
        Mockito.when(filmRepository.getFilm(1)).thenReturn(null);
        filmService.rentFilm(1);
    }
    @Test(expected = NotInStockException.class)
    public void filmRentStockEmpty(){
        Film MockFilm = new Film(5,"The Goonies",0);
        Mockito.when(filmRepository.getFilm(5)).thenReturn(MockFilm);
        filmService.rentFilm(5);
    }
    @Test
    public void ReductionInStock(){
        Film mockFilm = new Film(5, "The Goonies",5);
        Mockito.when(filmRepository.getFilm(5)).thenReturn(mockFilm);
        assertEquals(5,mockFilm.getCopiesInStock());
        filmService.rentFilm(5);
        assertEquals(4,mockFilm.getCopiesInStock());
    }
    @Test
    public void testReview(){
        Film mockFilm = org.mockito.Mockito.mock(Film.class);
        Mockito.when(mockFilm.getTotalScore()).thenReturn(6);
        Mockito.when(mockFilm.getNumReviews()).thenReturn(3);
        assertEquals(2, filmService.getAverageRating(mockFilm));
    }
    @Test
    public void testCatalog(){

    }




}
