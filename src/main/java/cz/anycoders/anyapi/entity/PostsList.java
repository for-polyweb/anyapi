package cz.anycoders.anyapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PostsList {
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

    @JsonIgnore
    @Getter
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rate> rating;

    public Integer getRatedCount(){
        return getRating().size();
    }
    private LocalDateTime created;
    private LocalDateTime updated;

    public double getAverageRating(){
        List<Rate> ratings = getRating();
        double summ = 0;
        for (Rate item : ratings) {
            summ += item.getValue(); // Assuming item.getValue() returns a Float or double
        }
        return ((double) Math.round((summ / ratings.size()) * 100) / 100);
    }


}
