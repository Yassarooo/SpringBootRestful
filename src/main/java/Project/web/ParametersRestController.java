package Project.web;

import Project.domain.Parameters;
import Project.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ParametersRestController {
    @Autowired
    private ParamsService paramsService;

    /**
     * @return
     */
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    public List<Parameters> params() {
        return paramsService.getAllParams();
    }
    /**
     * @return
     */
    @RequestMapping(value = "/paramsbyid", method = RequestMethod.GET)
    public List<Parameters> paramsById() {
        return paramsService.getAllParamsOrderBy(Comparator.comparing(Parameters::getId));
    }
    /**
     * @return
     */
    @RequestMapping(value = "/paramsbyname", method = RequestMethod.GET)
    public List<Parameters> paramsByName() {
        return paramsService.getAllParamsOrderBy(Comparator.comparing(Parameters::getName));
    }
    /**
     * @return
     */
    @RequestMapping(value = "/paramsbyseats", method = RequestMethod.GET)
    public List<Parameters> paramsBySeats() {
        return paramsService.getAllParamsOrderBy(Comparator.comparing(Parameters::getSeats));
    }
    /**
     * @return
     */
    @RequestMapping(value = "/paramsbypercent", method = RequestMethod.GET)
    public List<Parameters> paramsByPercent() {
        return paramsService.getAllParamsOrderBy(Comparator.comparing(Parameters::getPercentage));
    }

    /**
     * Web service for getting a parameters entity by id
     *
     * @param id param id
     * @return param
     */
    @RequestMapping(value = "/params/{id}", method = RequestMethod.GET)
    public ResponseEntity<Parameters> userByUserName(@PathVariable Long id) {
        Parameters param = paramsService.getParamById(id);
        if (param == null) {
            return new ResponseEntity<Parameters>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Parameters>(param, HttpStatus.OK);
        }
    }

    /**
     * Method for deleting a parameters entity by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/params/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Parameters> deleteParam(@PathVariable Long id) {
        Parameters param = paramsService.getParamById(id);
        if (param == null) {
            return new ResponseEntity<Parameters>(HttpStatus.NO_CONTENT);
        } else {
            paramsService.delete(param.getId());
            return new ResponseEntity<Parameters>(param, HttpStatus.OK);
        }

    }

    /**
     * Method for adding a parameters entity
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/params", method = RequestMethod.POST)
    public ResponseEntity<Parameters> createParam(@RequestBody Parameters param) {
        return new ResponseEntity<Parameters>(paramsService.createOrUpdateParam(param,false), HttpStatus.CREATED);
    }

    /**
     *
     * @param param
     * @return modified param
     */
    @RequestMapping(value = "/params", method = RequestMethod.PUT)
    public Parameters updateParam(@RequestBody Parameters param) {
        if (paramsService.getParamById(param.getId()) != null
                && paramsService.getParamById(param.getId()).getId() != param.getId()) {
            throw new RuntimeException("param already exist");
        }
        return paramsService.createOrUpdateParam(param,true);
    }

}
