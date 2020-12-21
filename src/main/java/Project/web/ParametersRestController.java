package Project.web;

import Project.domain.Parameters;
import Project.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return (List<Parameters>) paramsService.getAllParams();
    }

    /**
     * Web service for getting a parameters entity by id
     *
     * @param id param id
     * @return param
     */
    @RequestMapping(value = "/params/{id}", method = RequestMethod.GET)
    public ResponseEntity<Parameters> userByUserName(@PathVariable Long id) {
        Parameters param = paramsService.getParamsById(id);
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
        Parameters param = paramsService.getParamsById(id);
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
        return new ResponseEntity<Parameters>(paramsService.createOrUpdateParams(param,false), HttpStatus.CREATED);
    }

    /**
     *
     * @param param
     * @return modified param
     */
    @RequestMapping(value = "/params", method = RequestMethod.PUT)
    public Parameters updateParam(@RequestBody Parameters param) {
        if (paramsService.getParamsById(param.getId()) != null
                && paramsService.getParamsById(param.getId()).getId() != param.getId()) {
            throw new RuntimeException("param already exist");
        }
        return paramsService.createOrUpdateParams(param,true);
    }

}