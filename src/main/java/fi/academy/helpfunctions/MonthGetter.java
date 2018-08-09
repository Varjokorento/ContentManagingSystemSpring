package fi.academy.helpfunctions;

import fi.academy.models.Post;
import fi.academy.repositories.PostRepository;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class MonthGetter {

    public static List<String> findMonths(PostRepository postRepository) {
        List <Post> posts = postRepository.findAll();
        List<Integer> months = new ArrayList<>();
        for(int i = 0; i < posts.size(); i++ ) {
            if(!(months.contains(posts.get(i).getDate().getMonth()))) {
                months.add(posts.get(i).getDate().getMonth());
            }
        }
        List<String> monthsName = new ArrayList<>();
        for(int a = 0; a < months.size(); a++) {
            String name = returnMonthName(months.get(a));
            monthsName.add(name);
        }
        return monthsName;
    }

    public static String returnMonthName(int month) {

        return new DateFormatSymbols().getMonths()[month];
    }

}



