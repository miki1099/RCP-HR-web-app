package pl.uginf.rcphrwebapp.hr.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.document.dto.DocumentDTO;
import pl.uginf.rcphrwebapp.hr.document.service.DocumentService;
import pl.uginf.rcphrwebapp.hr.user.dto.BasicUserRecord;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserService;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@AllArgsConstructor
@RequestMapping("/hr/user")
public class HrController {

    UserService userService;

    DocumentService documentService;

    @GetMapping("/getAllActive")
    public List<UserDto> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }

    @GetMapping("/getAllManagers")
    public List<BasicUserRecord> getAllManagers() {
        return userService.getAllManagers();
    }

    @PostMapping("/addMangerForUser")
    public void addManagerForUser(@RequestParam("username") String username, @RequestParam String managerUsername) {
        userService.addManagerForUser(username, managerUsername);
    }

    @PostMapping(value = "/deactivate-user")
    public void deactivateUser(@RequestParam("user") String username) {
        userService.deactivateUser(username);
    }

    @PutMapping(value = "/create-user")
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @PostMapping(value = "/add-work-info")
    public WorkInfoDto addWorkInfo(@RequestBody WorkInfoDto workInfoDto) {
        return userService.addWorkInfo(workInfoDto);
    }

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String username) throws IOException {
        documentService.storeFile(file, username);
    }

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {

        DocumentDTO resource = documentService.getFile(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = resource.getFilename();
        headers.setContentDispositionFormData(filename, filename);
        return new ResponseEntity<>(resource.getData(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/deleteFile/{filename}")
    public void deleteFile(@PathVariable String filename) {
        documentService.deleteFile(filename);
    }

    @GetMapping("/getFile/{username}")
    public List<String> getFileForUser(@PathVariable String username) {
        return documentService.getAllFilenameForUser(username);
    }

}
