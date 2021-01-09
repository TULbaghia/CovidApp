package pl.lodz.p.mobi.covidapp.instruction;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.persistance.SQLiteHelper;

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

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(questionnaire.getQuestionList().size());
        view.findViewById(R.id.sayNo).setOnClickListener(v -> updateView(false));
        view.findViewById(R.id.sayYes).setOnClickListener(v -> updateView(true));
        renderView();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((InstructionFragment) getTargetFragment()).getQuestionnaires();
    }

    private void updateView(boolean status) {
        questionnaire.solve(currentQuestion, status);
        if (questionnaire.getQuestionList().size() > currentQuestion + 1) {
            currentQuestion++;
            renderView();
        } else {
            TextView textView = getView().findViewById(R.id.questionTitle);
            textView.setText(questionnaire.formatAnswer(getContext()));

            getView().findViewById(R.id.sayYes).setVisibility(View.GONE);
            getView().findViewById(R.id.sayNo).setVisibility(View.GONE);

            Button endButton = getView().findViewById(R.id.endButton);
            endButton.setVisibility(View.VISIBLE);
            endButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(questionnaire.formatColor())));
            endButton.setOnClickListener(x -> this.dismiss());

            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(questionnaire.formatColor())));

            SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
            sqLiteHelper.addQuestionnairesResults(questionnaire);
        }
    }

    private void renderView() {
        TextView textView = getView().findViewById(R.id.questionTitle);
        textView.setText(questionnaire.getQuestionList().get(currentQuestion).getTitle());

        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setProgress(currentQuestion + 1);

        TextView progressTextView = getView().findViewById(R.id.progressTextView);
        progressTextView.setText(getString(R.string.progress, currentQuestion + 1, questionnaire.getQuestionList().size()));

        ImageView questionImageView = getView().findViewById(R.id.questionImageView);
        questionImageView.setImageResource(questionnaire.getQuestionList().get(currentQuestion).getImageResource());
    }
}