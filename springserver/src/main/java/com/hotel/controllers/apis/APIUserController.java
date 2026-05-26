package com.hotel.controllers.apis;

import com.hotel.dto.UserDTO;
import com.hotel.dto.UserInfoDTO;
import com.hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class APIUserController {
    @Autowired
    private UserService userService;


    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "id") int id) {
        this.userService.delete(id);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMultiUser(@RequestBody List<Map<String, String>> listCustomerDelete) {
        List<Integer> ids = new ArrayList<>();
        listCustomerDelete.forEach(item -> {
            ids.add(Integer.valueOf(item.get("id")));
        });

        if (ids.isEmpty())
            return;

        this.userService.delete(ids);
    }

    @GetMapping("/secure/profile")
    @ResponseBody
    public ResponseEntity<UserDTO> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @PostMapping(path = "/secure/update/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addOrUpdateUser(@ModelAttribute UserInfoDTO infoDTO, Principal principal) {
        UserDTO avaiUser = this.userService.getUserByUsername(principal.getName());
        infoDTO.setId(avaiUser.getId());
        userService.updateInfoUser(infoDTO);
        return ResponseEntity.ok().build();
    }
}
