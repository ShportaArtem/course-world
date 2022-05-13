package ua.nure.shporta.dto;

import java.util.ArrayList;
import java.util.List;

public class AnswersDTO {

    public AnswersDTO(){
        super();
        answers = new ArrayList<>();
    }
    private List<AnswerDTO> answers;

    public void addAnswer(AnswerDTO answer){
        answers.add(answer);
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}
