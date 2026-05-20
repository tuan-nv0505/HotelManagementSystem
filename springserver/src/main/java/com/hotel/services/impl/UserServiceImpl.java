package com.hotel.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hotel.converter.UserConverter;
import com.hotel.dto.UserDTO;
import com.hotel.entity.User;
import com.hotel.entity.User;
import com.hotel.enums.RoleUser;
import com.hotel.repositories.UserRepository;
import com.hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("UserDetailService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Không tồn tại!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    @Override
    public List<UserDTO> listUser(Map<String, String> params) {
        List<User> userList = userRepository.listUser(params);
        List<UserDTO> listUser = new ArrayList<>();
        for (User c : userList) {
            UserDTO userDTO = userConverter.toUserDTO(c);
            String roleUser = RoleUser.getValue(c.getRole());
            userDTO.setRoleDisplay(roleUser);

            listUser.add(userDTO);
        }
        return listUser;
    }

    @Override
    public long countUser(Map<String, String> params) {
        return userRepository.countUser(params);
    }

    @Override
    public void addOrUpdateUser(UserDTO userDTO) {
        User user = userConverter.toUser(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        if (!userDTO.getFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(userDTO.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                user.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, "Lỗi upload avatar", ex);
                throw new RuntimeException("Lỗi hệ thống: Không thể tải lên ảnh đại diện!", ex);
            }
        }

        this.userRepository.addOrUpdateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepository.deleteUser(id);
    }

    @Override
    public void deleteUser(List<Integer> ids) {
        this.userRepository.deleteUser(ids);
    }
}
