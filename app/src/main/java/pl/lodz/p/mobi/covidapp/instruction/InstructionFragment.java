package pl.lodz.p.mobi.covidapp.instruction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.persistance.SQLiteHelper;

public class InstructionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getParentFragmentManager();

        view.findViewById(R.id.startTest).setOnClickListener(view1 -> {
            DialogFragment dialogFragment = new QuestionnaireFragment();
            dialogFragment.setTargetFragment(this, 1);
            dialogFragment.show(fm, "Sample Fragment");
        });

        getQuestionnaires();

        view.findViewById(R.id.mzImageView).setOnClickListener(v -> {
            startIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mz_url))));
        });

        view.findViewById(R.id.twitterImageView).setOnClickListener(v -> {
            startIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mz_twitter_url))));
        });

        view.findViewById(R.id.facebookImageView).setOnClickListener(v -> {
            startIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mz_facebook_url))));
        });
    }

    private void startIntent(@NonNull Intent intent) {
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void getQuestionnaires() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        Map<Integer, Pair<String, Questionnaire>> questionnaires = sqLiteHelper.getQuestionnairesResults();

        LinearLayout ll = getView().findViewById(R.id.questionnariesLayout);
        for (int i = 0; i < questionnaires.size(); i++) {
            if(ll.getChildAt(i) instanceof TextView) {
                String input = questionnaires.get(i).first + "\n" + questionnaires.get(i).second.formatAnswer(getContext());
                ((TextView) ll.getChildAt(i)).setText(input);
            }
        }
    }
}
