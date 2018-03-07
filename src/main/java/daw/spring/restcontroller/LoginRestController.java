package daw.spring.restcontroller;

import daw.spring.utilities.ApiRestController;

@ApiRestController
public class LoginRestController {

    /* private final UserService userService;

    @Autowired
    public LoginRestController (UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> usersList = userService.findAll();

        if (usersList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User user = userService.findOneById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }*/

    // TODO add UserComponent to handle user states when login

    // TODO add interfaces for represent the api here and @JsonView (using interfaces) on attributes

    // TODO login and logOut methods using RequestMapping
}
