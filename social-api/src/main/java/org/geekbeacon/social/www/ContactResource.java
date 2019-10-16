package org.geekbeacon.social.www;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.geekbeacon.social.model.Contact;
import org.geekbeacon.social.service.ContactService;

import java.util.List;


@RestController
@Log4j2
public class ContactResource {

    private final ContactService contactService;

    @Autowired
    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("v1.0/contact/list")
    public List<Contact> contactEmails(
        @RequestParam(name = "domain", defaultValue = "nixiepixel.com") String domain) {
        return contactService.getContactListing(domain);

    }
}
