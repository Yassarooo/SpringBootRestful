package Project.service;

import java.util.*;
import java.util.logging.Logger;

import Project.aspect.UserAspect;
import Project.domain.AppUser;
import Project.domain.Car;
import Project.domain.Parameters;
import Project.repository.CarRepository;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    ParamsService paramsService;

    @Autowired
    JwtUserDetailsService userService;

    private Authentication auth;

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
            auth = SecurityContextHolder.getContext().getAuthentication();

            c.setBrandlogo(this.setBrandLogo(c.getBrand().toLowerCase().trim()));
            Optional<Car> car;

            if (update) {
                car = carRepository.findById(c.getId());
                if (car.isPresent()) {

                    Parameters def = paramsService.getParamById((long) c.getParamid());
                    if (def == null)
                        throw new RuntimeException("No default params record exist for given id " + c.getId());
                    c.setParams(def);

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
                        newEntity.setUser(c.getUser());
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
                AppUser user = userService.findUserByUsername(auth.getName());
                if (user != null) {
                    c.setUser(user);
                    List<Car> cars = new ArrayList<Car>(user.getCars());
                    cars.add(c);
                    carRepository.save(c);
                    user.setCars(cars);
                    userService.updateUser(user);
                    log.info("CarService User != null cars length :" +user.getCars().size());
                }
                return carRepository.save(c);
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
            return "https://www.vhv.rs/dpng/f/57-576848_whatsapp-logo-png-transparent-background.png";
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
            return "https://www.stickpng.com/img/download/584831f6cef1014c0b5e4aa6/image";
        else if (brand.contains("ferrari"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c52.png";
        else if (brand.contains("lamborghini"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c8c.png";
        else if (brand.contains("maserati"))
            return "https://pngimg.com/uploads/maserati/maserati_PNG73.png";
        else if (brand.contains("lada"))
            return "https://pngimg.com/uploads/lada/lada_PNG111.png";
        else if (brand.contains("mitsubishi"))
            return "https://logos-download.com/wp-content/uploads/2016/02/Mitsubishi_logo_standart.png";
        else if (brand.contains("volvo"))
            return "https://pngimg.com/uploads/volvo/volvo_PNG64.png";
        else if (brand.contains("nissan"))
            return "https://purepng.com/public/uploads/large/purepng.com-nissan-logonissannissan-motorautomobile-manufactureryokohamanissan-logo-1701527528584gkr0t.png";
        else if (brand.contains("ford"))
            return "https://logos-download.com/wp-content/uploads/2016/02/Ford_logo_motor_company_transparent.png";
        else if (brand.contains("genesis"))
            return "https://toppng.com/download/TT5bPfBbviPB7Q9p5VeCWX1QgaHQhy3hCHGQUyAM5tBrfqdMlrE9wlwDv1ABrzSdHNMTFGvOFTD2P5rVtv1UU6auXD4crYanrhRQrjvpWuu915YEQc8GuW5004tiXwkIoZYCv2GAbn51St7gU2EiBFL9gcPpJO4x5RLJ6Lfk7DJE94iA8OIy1qQuJ2sv0x6NjdLm0WMu/large";
        else if (brand.contains("mazda"))
            return "https://pngimg.com/uploads/mazda/mazda_PNG86.png";
        else if (brand.contains("citroen"))
            return "https://upload.wikimedia.org/wikipedia/commons/0/0b/Citroen-logo-2009.png";
        else if (brand.contains("jeep"))
            return "https://r1.hiclipart.com/path/427/594/445/9adse6qvto3ge4jqsvjqs0nb7n.png";
        else if (brand.contains("chevrolet"))
            return "https://pngimg.com/uploads/chevrolet/%D1%81hevrolet_PNG109.png";
        else if (brand.contains("audi"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c18.png";
        else if (brand.contains("gmc"))
            return "https://logodownload.org/wp-content/uploads/2019/08/gmc-logo-0.png";
        else if (brand.contains("land rover"))
            return "https://toppng.com/download/EzQhs5hh8uPdDBA1nS7rXB1QbtzJPvajyvSTtAPpbL2QoQeyu8bOJc4xaRwaaqz7gTtz3Igkb0wQi1rr1OJnr1R9DZi8zUxmXXty55xV4WUuF5879hAfDONf14X9gDfHfGWNmiwK96t4dUh9sCkYF4T4aR2Vm6YyphWK5aApxxksLX9HjK2tg0Qg5YVCivIub5xR9ImC/large";
        else if (brand.contains("bentley"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2c2c.png";
        else if (brand.contains("fiat"))
            return "http://www.pngall.com/wp-content/uploads/5/Fiat-Logo-Transparent.png";
        else if (brand.contains("lexus"))
            return "http://pngimg.com/uploads/lexus/lexus_PNG37.png";
        else if (brand.contains("subaru"))
            return "https://assets.stickpng.com/images/580b585b2edbce24c47b2cbf.png";
        else if (brand.contains("bugatti"))
            return "https://pngimg.com/uploads/bugatti_logo/bugatti_logo_PNG5.png";
        else if (brand.contains("infiniti"))
            return "https://www.stickpng.com/img/download/580b57fcd9996e24bc43c483/image";
        else if (brand.contains("suzuki"))
            return "https://assets.stickpng.com/images/580b57fcd9996e24bc43c4a4.png";
        else if (brand.contains("opel"))
            return "https://pngimg.com/uploads/opel/opel_PNG11.png";
        else if (brand.contains("hummer"))
            return "https://logodix.com/logo/91857.png";
        else if (brand.contains("skoda"))
            return "https://www.stickpng.com/img/download/580b585b2edbce24c47b2cb7/image";
        else if (brand.contains("porsche"))
            return "https://www.stickpng.com/img/download/580b585b2edbce24c47b2cac/image";
        else if (brand.contains("renault"))
            return "https://pngimg.com/uploads/renault/renault_PNG39.png";
        else if (brand.contains("rolls royce"))
            return "https://pngimg.com/uploads/rolls_royce/rolls_royce_PNG34.png";
        else if (brand.contains("mclaren"))
            return "https://pngimg.com/uploads/Mclaren/Mclaren_PNG49.png";
        else if (brand.contains("geely"))
            return "https://www.carlogos.org/logo/Geely-logo-2014-2560x1440.png";
        else if (brand.contains("acura"))
            return "https://pngimg.com/uploads/acura/acura_PNG80.png";
        else if (brand.contains("alfa romeo"))
            return "https://pngimg.com/uploads/alfa_romeo/alfa_romeo_PNG71.png";
        else if (brand.contains("aston martin"))
            return "http://pngimg.com/uploads/aston_martin/aston_martin_PNG17.png";
        else if (brand.contains("cadillac"))
            return "https://pngimg.com/uploads/cadillac/cadillac_PNG34.png";
        else if (brand.contains("chrysler"))
            return "https://pngimg.com/uploads/chrysler/chrysler_PNG28.png";
        else if (brand.contains("dodge"))
            return "https://pngimg.com/uploads/dodge/dodge_PNG47.png";
        else if (brand.contains("koenigsegg"))
            return "https://www.stickpng.com/img/download/580b57fcd9996e24bc43c486/image";
        else if (brand.contains("lancia"))
            return "http://pngimg.com/uploads/car_logo/car_logo_PNG1650.png";
        else if (brand.contains("maybach"))
            return "https://1000logos.net/wp-content/uploads/2020/04/Maybach_logo_PNG2.png";
        else
            return "";
    }


}