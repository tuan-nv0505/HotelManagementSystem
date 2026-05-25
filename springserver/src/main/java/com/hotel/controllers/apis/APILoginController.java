package com.hotel.controllers.apis;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hotel.dto.UserDTO;
import com.hotel.services.UserService;
import com.hotel.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class APILoginController {
    @Autowired
    private UserService userService;

    @Value("${GOOGLE_CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;
    @Value("${FACEBOOK_APP_ID}")
    private String FACEBOOK_APP_ID;
    @Value("${FACEBOOK_APP_SECRET}")
    private String FACEBOOK_APP_SECRET;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        if (this.userService.authenticate(userDTO.getUsername(), userDTO.getPassword())) {
            try {
                String token = JwtUtils.generateToken(userDTO);
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @ModelAttribute(name = "userDTO") UserDTO userDTO, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });

            response.put("status", "FAIL");
            response.put("message", "Dữ liệu đầu vào không hợp lệ");
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            userService.addOrUpdate(userDTO);
            response.put("status", "SUCCESS");
            response.put("message", "Tạo tài khoản thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("idToken");
        Map<String, Object> response = new HashMap<>();
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String pictureUrl = (String) payload.get("picture");

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(payload.getEmail());
                userDTO.setName((String) payload.get("name"));
                userDTO.setUsername(payload.getEmail());
                userDTO.setAvatar(pictureUrl);
                userDTO.setPhone("0000000000");
                String randomPassword = java.util.UUID.randomUUID().toString();
                userDTO.setPassword(randomPassword);
                userService.addOrUpdateSocial(userDTO);
                String jwtToken = JwtUtils.generateToken(userDTO);

                response.put("status", "SUCCESS");
                response.put("message", "Đăng nhập Google thành công");
                response.put("token", jwtToken);
                response.put("user", userDTO);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "FAIL");
                response.put("message", "Token Google không hợp lệ");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "ERROR");
            response.put("message", "Lỗi server: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/facebook")
    public ResponseEntity<Map<String, Object>> loginWithFacebook(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        Map<String, Object> response = new HashMap<>();
        try {
            String appAccessToken = FACEBOOK_APP_ID + "|" + FACEBOOK_APP_SECRET;
            String debugUrl = "https://graph.facebook.com/debug_token?input_token=" + accessToken + "&access_token=" + appAccessToken;

            URL urlDebug = new URL(debugUrl);
            HttpURLConnection conDebug = (HttpURLConnection) urlDebug.openConnection();
            conDebug.setRequestMethod("GET");

            if (conDebug.getResponseCode() != 200) {
                response.put("status", "FAIL");
                response.put("message", "Lỗi xác minh token với Facebook.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            BufferedReader inDebug = new BufferedReader(new InputStreamReader(conDebug.getInputStream()));
            String outputDebug;
            StringBuilder resultDebug = new StringBuilder();
            while ((outputDebug = inDebug.readLine()) != null) {
                resultDebug.append(outputDebug);
            }
            inDebug.close();

            com.google.gson.JsonObject debugJson = com.google.gson.JsonParser.parseString(resultDebug.toString()).getAsJsonObject();
            com.google.gson.JsonObject dataJson = debugJson.getAsJsonObject("data");

            if (!dataJson.has("app_id") || !dataJson.get("app_id").getAsString().equals(FACEBOOK_APP_ID)) {
                response.put("status", "FAIL");
                response.put("message", "Cảnh báo bảo mật: Token không thuộc về ứng dụng này!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!dataJson.get("is_valid").getAsBoolean()) {
                response.put("status", "FAIL");
                response.put("message", "Token Facebook đã hết hạn hoặc bị thu hồi.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String fbGraphUrl = "https://graph.facebook.com/me?fields=id,name,email,picture.width(200).height(200)&access_token=" + accessToken;
            URL url = new URL(fbGraphUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String output;
                StringBuilder result = new StringBuilder();
                while ((output = in.readLine()) != null) {
                    result.append(output);
                }
                in.close();

                String jsonResponse = result.toString();
                com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();

                String facebookId = jsonObject.get("id").getAsString();
                String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : "fb_" + facebookId;

                String pictureUrl = "";
                if (jsonObject.has("picture")) {
                    com.google.gson.JsonObject pictureObj = jsonObject.getAsJsonObject("picture");
                    if (pictureObj.has("data")) {
                        com.google.gson.JsonObject dataObj = pictureObj.getAsJsonObject("data");
                        pictureUrl = dataObj.has("url") ? dataObj.get("url").getAsString() : "";
                    }
                }
                String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : facebookId + "@facebook.com";
                String phone = jsonObject.has("phone") ? jsonObject.get("phone").getAsString() : "0000000000";

                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(name);
                userDTO.setAvatar(pictureUrl);
                userDTO.setEmail(email);
                userDTO.setPhone(phone);

                userService.addOrUpdateSocial(userDTO);
                String jwtToken = JwtUtils.generateToken(userDTO);

                response.put("status", "SUCCESS");
                response.put("message", "Đăng nhập Facebook thành công");
                response.put("token", jwtToken);
                response.put("user", userDTO);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "FAIL");
                response.put("message", "Token Facebook không hợp lệ");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Lỗi server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
