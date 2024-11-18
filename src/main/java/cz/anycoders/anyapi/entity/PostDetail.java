package cz.anycoders.anyapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class PostDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToOne
    @JoinColumn(name = "state_id")
    private State state;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User author;

    private String[] pros;
    private String[] cons;
    private String description;

    @OneToOne
    @JoinColumn(name = "type_id")
    private Type type;


    @Getter
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rate> rating;

    @Getter
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;


    public Integer getRatedCount(){
        List<Rate> rating = getRating();
        if(rating != null && !rating.isEmpty()){
            return rating.size();
        }
        return 0;
    }

    public double getAverageRating(){
        double result = 0f;
        if(getRatedCount()>0) {
            List<Rate> ratings = getRating();
            double summ = 0f;
            for (Rate item : ratings) {
                summ += item.getValue(); // Assuming item.getValue() returns a Float or double
            }
            result = ((double) Math.round((summ / ratings.size()) * 100) / 100);
        }

        return result;
    }


    private LocalDateTime created;
    private LocalDateTime updated;

}
