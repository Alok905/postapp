package com.alok.postapp.tools;

import com.alok.postapp.entity.Post;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestTool {

    Map<String, String> weatherCity = Map.of(
            "banglore", "Cloudy",
            "mumbai", "Rainy",
            "bhubaneswar", "Sunny",
            "kolkata", "Humid"
    );

    @Tool(name = "create_post", description = "create a post")
    public Post createPost(String title, String description) {
        System.out.println("Creating a post with title: " + title + " and description: " + description);
        return Post.builder().title(title).description(description).build();
    }

    @Tool(name = "city_weather", description = """
            get the current user of a city. 
            the city name should be in lowercase and it should of {"banglore", "mumbai", "bhubaneswar", "kolkata"}
            """)
    public String getCityWeather(String city) {
        System.out.println("Getting the current weather of a city: " + city);
        return weatherCity.get(city.toLowerCase());
    }
}
