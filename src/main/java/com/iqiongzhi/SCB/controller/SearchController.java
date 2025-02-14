package com.iqiongzhi.SCB.controller;

import com.iqiongzhi.SCB.annotation.Auth;
import com.iqiongzhi.SCB.data.vo.Result;
import com.iqiongzhi.SCB.service.RecommendService;
import com.iqiongzhi.SCB.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private HttpServletRequest request;

    @Auth
    @GetMapping("/all")
    public ResponseEntity<Result> searchSounds(@RequestParam String keyword,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        String userId = (String) request.getAttribute("userId");
        return searchService.searchSound(userId, keyword, page, size);
    }

    @Auth
    @GetMapping("/history")
    public ResponseEntity<Result> searchHistory() {
        String userId = (String) request.getAttribute("userId");
        return searchService.searchHistory(userId);
    }

    @Auth
    @GetMapping("/author")
    public ResponseEntity<Result> searchAuthor(@RequestParam String keyword,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return searchService.searchAuthor(keyword, page, size);
    }
}
