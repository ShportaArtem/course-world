package ua.nure.shporta.dto;

public class AnswerDTO {
    private String question;
    private String answer;

    public AnswerDTO(){
        super();
    }

    public AnswerDTO(String question){
        super();
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
