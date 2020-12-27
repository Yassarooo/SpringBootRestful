package Project.Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Project.domain.Car;
import Project.domain.Parameters;
import Project.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;


public class CarServiceControllerTest extends AbstractTest {

    @InjectMocks
    private CarService carservice;

    @Mock
    private Parameters param;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getCarsTest() throws Exception {
        String uri = "/api/cars";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Car[] carlist = super.mapFromJson(content, Car[].class);
        assertTrue(carlist.length > 0);
    }

    @Test
    public void createCarTest() throws Exception {
        String uri = "/api/cars";
        Car car = new Car("test", 7540f, null, "yassar", 8000f, new Date(), true, 1);

        String inputJson = super.mapToJson(car);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    public void updateCar() throws Exception {
        String uri = "/api/cars";
        Car car = new Car("test", 7540f, 4, "yassar", 8000f, new Date(), true, 1);

        String inputJson = super.mapToJson(car);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void deleteCar() throws Exception {
        String uri = "/api/cars";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
}