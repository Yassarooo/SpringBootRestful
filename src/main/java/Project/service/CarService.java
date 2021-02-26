package Project.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Project.aspect.UserAspect;
import Project.domain.Car;
import Project.domain.Parameters;
import Project.repository.CarRepository;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    private final static Logger log = Logger.getLogger(UserAspect.class.getName());


    public List<Car> getAllCars() {
        List<Car> carList = (List<Car>) carRepository.findAll();

        if (carList.size() > 0) {

            return carList;
        } else {
            return new ArrayList<Car>();
        }
    }

    public List<Car> getAllCarsOrderBy(Comparator<Car> c) {
        List<Car> carList = (List<Car>) carRepository.findAll();

        if (carList.size() > 0) {
            carList.sort(c);
            return carList;
        } else {
            return new ArrayList<Car>();
        }
    }


    public List<Car> getAllCarsForParamId(Long paramid) {
        List<Car> carList = (List<Car>) carRepository.findAllByparamid(paramid);

        if (carList.size() > 0) {

            return carList;
        } else {
            return new ArrayList<Car>();
        }
    }

    public Car getCarById(Long id) throws RuntimeException {
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            return car.get();
        } else {
            throw new RuntimeException("No car record exist for given id");
        }
    }

    @Transactional
    public Car createOrUpdateCar(Car c, Boolean update) throws RuntimeException {
        try {
            c.setBrandlogo(this.setBrandLogo(c.getBrand().toLowerCase().trim()));
            Optional<Car> car;
            if (update) {
                car = carRepository.findById(c.getId());
                if (car.isPresent()) {
                    Car newEntity = car.get();
                    if (c.getVersion() < newEntity.getVersion()) {
                        throw new OptimisticEntityLockException(c, "Optimistic Lock Error");
                    } else {
                        newEntity.setModel(c.getModel().trim());
                        newEntity.setBrand(c.getBrand().trim());
                        newEntity.setBrandlogo(c.getBrandlogo());
                        newEntity.setPrice(c.getPrice());
                        newEntity.setSeats(c.getSeats());
                        newEntity.setType(c.getParams().getName().trim());
                        newEntity.setParams(c.getParams());
                        newEntity.setBuyername(c.getBuyername().trim());
                        newEntity.setSelldate(c.getSelldate());
                        newEntity.setSellprice(c.getSellprice());
                        newEntity.setSold(c.getSold());
                        newEntity.setSpecs(c.getSpecs());
                        newEntity.setSpecsid(c.getSpecsid() == -1 ? newEntity.getSpecsid() : c.getSpecsid());
                        newEntity.setLevel(c.getLevel().trim());
                        newEntity.setRate(c.getRate());
                        newEntity.setYear(c.getYear());
                        newEntity.setImages(c.getImages());
                        newEntity = carRepository.save(newEntity);
                        return newEntity;
                    }
                } else {
                    throw new RuntimeException("No car record exist for given id " + c.getId());
                }
            } else {
                c = carRepository.save(c);

                return c;
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            log.severe("Somebody has already updated the amount for item:{} in concurrent transaction.");
            throw e;
        }
    }

    @Transactional
    public void deleteCarById(Long id) throws RuntimeException {
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            carRepository.deleteById(id);
        } else {
            throw new RuntimeException("No car record exist for given id");
        }
    }

    private String setBrandLogo(String brand) throws RuntimeException {
        if (brand.contains("bmw"))
            return "https://www.pinclipart.com/picdir/big/361-3619725_bmw-clip-art.png";
        else if (brand.contains("kia"))
            return "https://cdn.freebiesupply.com/logos/large/2x/kia-logo-png-transparent.png";
        else if (brand.contains("toyota"))
            return "https://png2.cleanpng.com/sh/27c9cb54c28631de05db65752a34b6a3/L0KzQYm3WMEyN6dwj5H0aYP2gLBuTgRwgZD5eZ8DNj3mccPrif5idJZ8ees2bHBqf368gsZnQZZme6RtMHW6RXA4WMQ2O2c9UaMAM0SzRIKAV8IyQGU7RuJ3Zx==/kisspng-toyota-86-cardinaleway-logo-5b6f9eac2d0e75.1845368915340417721846.png";
        else if (brand.contains("mercedes"))
            return "https://cdn.freebiesupply.com/logos/large/2x/mercedes-benz-9-logo-png-transparent.png";
        else if (brand.contains("tesla"))
            return "https://www.carlogos.org/car-logos/tesla-logo-2200x2800.png";
        else if (brand.contains("honda"))
            return "https://www.freeiconspng.com/uploads/honda-logo-transparent-background-0.jpg";
        else if (brand.contains("hyundai"))
            return "https://logos-download.com/wp-content/uploads/2016/03/Hyundai_Motor_Company_logo.png";
        else if (brand.contains("jaguar"))
            return "https://freepngimg.com/thumb/car%20logo/12-jaguar-car-logo-png-brand-image.png";
        else if (brand.contains("volkswagen"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2cf2.png";
        else if (brand.contains("peageot"))
            return "https://pngimg.com/uploads/peugeot/peugeot_PNG36.png";
        else if (brand.contains("ferrari"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c52.png";
        else if (brand.contains("lamborghini"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c8c.png";
        else if (brand.contains("maserati"))
            return "https://banner2.cleanpng.com/20180603/ili/kisspng-maserati-granturismo-car-luxury-vehicle-maserati-q-5b13b75a7c2586.7844536415280187785085.jpg";
        else if (brand.contains("lada"))
            return "https://pngimg.com/uploads/lada/lada_PNG111.png";
        else if (brand.contains("mitsubishi"))
            return "https://logos-download.com/wp-content/uploads/2016/02/Mitsubishi_logo_standart.png";
        else if (brand.contains("volvo"))
            return "https://pngimg.com/uploads/volvo/volvo_PNG64.png";
        else if (brand.contains("nissan"))
            return "https://logos-download.com/wp-content/uploads/2016/02/Nissan_logo_2.jpg";
        else if (brand.contains("ford"))
            return "https://logos-download.com/wp-content/uploads/2016/02/Ford_logo_motor_company_transparent.png";
        else if (brand.contains("mazda"))
            return "https://pngimg.com/uploads/mazda/mazda_PNG86.png";
        else if (brand.contains("citroen"))
            return "https://upload.wikimedia.org/wikipedia/commons/0/0b/Citroen-logo-2009.png";
        else if (brand.contains("jeep"))
            return "https://mpng.subpng.com/20181118/eqw/kisspng-jeep-car-logo-brand-portable-network-graphics-5bf0fe9dc315f0.1693876715425204777991.jpg";
        else if (brand.contains("chevrolet"))
            return "https://pngimg.com/uploads/chevrolet/%D1%81hevrolet_PNG109.png";
        else if (brand.contains("audi"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c18.png";
        else if (brand.contains("gmc"))
            return "https://logodownload.org/wp-content/uploads/2019/08/gmc-logo-0.png";
        else if (brand.contains("land rover"))
            return "https://pngimg.com/uploads/land_rover/land_rover_PNG39.png";
        else if (brand.contains("bentley"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c2c.png";
        else if (brand.contains("fiat"))
            return "http://www.pngall.com/wp-content/uploads/5/Fiat-Logo-Transparent.png";
        else if (brand.contains("lexus"))
            return "https://assets.stickpng.com/images/580b57fcd9996e24bc43c48b.png";
        else if (brand.contains("subaru"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2cbf.png";
        else if (brand.contains("bugatti"))
            return "https://pngimg.com/uploads/bugatti_logo/bugatti_logo_PNG5.png";
        else if (brand.contains("infinity"))
            return "https://image.pngaaa.com/217/882217-middle.png";
        else if (brand.contains("suzuki"))
            return "https://assets.stickpng.com/images/580b57fcd9996e24bc43c4a4.png";
        else if (brand.contains("opel"))
            return "http://assets.stickpng.com/images/580b57fcd9996e24bc43c49c.png";
        else if (brand.contains("hummer"))
            return "https://logodix.com/logo/91857.png";
        else
            return "";
    }


}