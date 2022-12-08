package pl.uginf.rcphrwebapp.hr.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.hr.document.dto.DocumentDTO;
import pl.uginf.rcphrwebapp.hr.document.service.DocumentService;
import pl.uginf.rcphrwebapp.hr.user.dto.UserDto;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

@RestController
@AllArgsConstructor
@RequestMapping("/hr/user")
public class HrController {

    UserSLO userSLO;

    DocumentService documentService;

    @PostMapping(value = "/deactivate-user")
    public void deactivateUser(@RequestParam("user") String username) {
        userSLO.deactivateUser(username);
    }

    @PutMapping(value = "/create-user")
    public UserDto createUser(@RequestBody UserDto user) {
        return userSLO.addUser(user);
    }

    @PostMapping(value = "/add-work-info")
    public WorkInfoDto addWorkInfo(@RequestBody WorkInfoDto workInfoDto) {
        return userSLO.addWorkInfo(workInfoDto);
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

    @DeleteMapping("/deleteFile/{fileName}")
    public void deleteFile(@PathVariable String fileName) {
        documentService.deleteFile(fileName);
    }

    //TODO pobieranie nazw plików dla usera

}
