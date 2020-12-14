package pl.lodz.p.mobi.covidapp.instruction;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire {
    private final List<Question> questionList = new ArrayList<>();

    public Questionnaire() {
        questionList.add((new Question()).setTitle("Czy masz kaszel?").setDescription("").setWeight(84));
//        questionList.add((new Question()).setTitle("Czy kaszel jest suchy?").setDescription("").setWeight());
        questionList.add((new Question()).setTitle("Czy masz gorączkę?").setDescription("").setWeight(80));
        questionList.add((new Question()).setTitle("Czy masz bóle mięśni?").setDescription("").setWeight(63));
        questionList.add((new Question()).setTitle("Czy masz dreszcze?").setDescription("").setWeight(63));
        questionList.add((new Question()).setTitle("Czy odczuwasz przemęczenie?").setDescription("").setWeight(62));
        questionList.add((new Question()).setTitle("Czy masz bóle głowy?").setDescription("").setWeight(59));
        questionList.add((new Question()).setTitle("Czy masz płytki oddech?").setDescription("").setWeight(57));
        questionList.add((new Question()).setTitle("Czy masz ból gardła?").setDescription("").setWeight(40));
        questionList.add((new Question()).setTitle("Czy masz katar?").setDescription("").setWeight(40));
        questionList.add((new Question()).setTitle("Czy masz biegunkę?").setDescription("").setWeight(38));
        questionList.add((new Question()).setTitle("Czy masz bóle w klatce piersiowej?").setDescription("").setWeight(23));
        questionList.add((new Question()).setTitle("Czy odczuwasz zmianę smaku lub węchu?").setDescription("").setWeight(20));
        questionList.add((new Question()).setTitle("Czy wymiotujesz?").setDescription("").setWeight(13));
        questionList.add((new Question()).setTitle("Czy masz wysypkę?").setDescription("").setWeight(5));
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
}
