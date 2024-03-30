package classes;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Order {
    List<String> ingredients = new ArrayList<>();

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

