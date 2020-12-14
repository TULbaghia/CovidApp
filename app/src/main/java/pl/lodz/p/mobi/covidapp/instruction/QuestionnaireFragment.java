package pl.lodz.p.mobi.covidapp.instruction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.lodz.p.mobi.covidapp.R;

public class QuestionnaireFragment extends DialogFragment {
    private Questionnaire questionnaire;
    int currentQuestion = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionnaire = new Questionnaire();

        view.findViewById(R.id.sayNo).setOnClickListener(v -> updateView(false));
        view.findViewById(R.id.sayYes).setOnClickListener(v -> updateView(true));
        renderView();
    }

    private void updateView(boolean status) {
        questionnaire.solve(currentQuestion, status);
        if (questionnaire.getQuestionList().size() > currentQuestion + 1) {
            currentQuestion++;
            renderView();
        } else {
            TextView textView = getView().findViewById(R.id.questionTitle);
            double answerValue = questionnaire.calculateAnswer();
            if(answerValue < 250) {
                textView.setText(getString(R.string.state_healthy));
            } else if (answerValue < 320) {
                textView.setText(getString(R.string.state_unsolvable));
            } else {
                textView.setText(getString(R.string.state_infected));
            }
            getView().findViewById(R.id.sayYes).setVisibility(View.GONE);
            getView().findViewById(R.id.sayNo).setVisibility(View.GONE);
            getView().findViewById(R.id.endButton).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.endButton).setOnClickListener(x -> this.dismiss());
        }
    }

    private void renderView() {
        TextView textView = getView().findViewById(R.id.questionTitle);
        textView.setText(questionnaire.getQuestionList().get(currentQuestion).getTitle());
    }

    // 1. Podaj wiek
    // 2. Czy masz kaszel? (TAK, NIE)         84%
    // 2a. Czy kaszel jest suchy? (TAK, NIE)   suchy 50% : mokry 25%
    // 3. Czy masz gorączkę? (TAK, NIE)    80%
    // 4. Czy masz bóle mięśni? (TAK, NIE) 63%
    // 5. Czy masz dreszcze? (TAK, NIE)    63%
    // 6. Czy odczuwasz przemęczenie? (TAK, NIE)   62%
    // 7. Czy masz bóle głowy? (TAK, NIE)    59%
    // 8. Czy masz płytki oddech? (TAK, NIE)  57%
    // 9. Czy masz ból gardła? (TAK, NIE)    40%
    // 10. Czy masz katar? (TAK, NIE)        40%
    // 11. Czy masz biegunkę? (TAK, NIE)        38%
    // 12. Czy masz bóle w klatce piersiowej? (TAK, NIE)   23%
    // 13. Czy odczuwasz zmianę smaku lub węchu? (TAK, NIE) 20%
    // 14. Czy wymiotujesz? (TAK, NIE)     13%
    // 15. Czy masz wysypkę? (TAK, NIE)   5%
}