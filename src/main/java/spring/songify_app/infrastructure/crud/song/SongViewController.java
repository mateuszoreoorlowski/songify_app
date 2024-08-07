package spring.songify_app.infrastructure.crud.song;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class SongViewController {

    @GetMapping("/")
    public String home(){
        return "home";
    }
}
