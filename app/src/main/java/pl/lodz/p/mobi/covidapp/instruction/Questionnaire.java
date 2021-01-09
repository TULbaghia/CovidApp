package pl.lodz.p.mobi.covidapp.instruction;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.lodz.p.mobi.covidapp.R;

public class Questionnaire {

    private UUID id;

    private final List<Question> questionList = new ArrayList<>();

    public Questionnaire() {
        this.setId();
        questionList.add((new Question()).setTitle("Czy masz kaszel?").setDescription("").setWeight(84).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy masz gorączkę?").setDescription("").setWeight(80).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy masz bóle mięśni?").setDescription("").setWeight(63).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy masz dreszcze?").setDescription("").setWeight(63).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy odczuwasz przemęczenie?").setDescription("").setWeight(62).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy masz bóle głowy?").setDescription("").setWeight(59).setImageResource(R.drawable.vaccinate_logo));
        questionList.add((new Question()).setTitle("Czy masz płytki oddech?").setDescription("").setWeight(57).setImageResource(R.drawable.vaccinate_logo));
        questionList.add((new Question()).setTitle("Czy masz ból gardła?").setDescription("").setWeight(40).setImageResource(R.drawable.vaccinate_logo));
        questionList.add((new Question()).setTitle("Czy masz katar?").setDescription("").setWeight(40).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy masz biegunkę?").setDescription("").setWeight(38).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy masz bóle w klatce piersiowej?").setDescription("").setWeight(23).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy odczuwasz zmianę smaku lub węchu?").setDescription("").setWeight(20).setImageResource(R.drawable.facebook_logo));
        questionList.add((new Question()).setTitle("Czy wymiotujesz?").setDescription("").setWeight(13).setImageResource(R.drawable.vaccinate_logo));
        questionList.add((new Question()).setTitle("Czy masz wysypkę?").setDescription("").setWeight(5).setImageResource(R.drawable.vaccinate_logo));
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void solve(int currentQuestion, boolean b) {
        questionList.get(currentQuestion).setResponse(b);
    }

    public double calculateAnswer() {
        return questionList.stream().filter(Question::isResponse).mapToDouble(Question::getWeight).sum();
    }

    public String formatAnswer(Context context) {
        double score = calculateAnswer();
        if(score < 250) {
            return context.getString(R.string.state_healthy);
        } else if (score < 320) {
            return context.getString(R.string.state_unsolvable);
        } else {
            return context.getString(R.string.state_infected);
        }
    }

    public String formatColor() {
        double score = calculateAnswer();
        if(score < 250) {
            return "#5CB85C";
        } else if (score < 320) {
            return "#F0AD4E";
        } else {
            return "#D9534F";
        }
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", questionList=" + questionList +
                '}';
    }
}
