package com.mfd.controller.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "chat")
public class ChatController {



    @RequestMapping(value = "/page")
    public String page(){
        return "chat/chatPage";
    }


}
