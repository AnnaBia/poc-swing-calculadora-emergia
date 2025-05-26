package main.model;

public class ProcessoIA {
    private final String nome;
    private final FaseIA fase;
    private final int inferencias;
    private final int horas;

    public ProcessoIA(String nome, FaseIA fase, int inferencias, int horas) {
        this.nome = nome;
        this.fase = fase;
        this.inferencias = inferencias;
        this.horas = horas;
    }

    public String getNome() { return nome; }
    public FaseIA getFase() { return fase; }
    public int getInferencias() { return inferencias; }
    public int getHoras() { return horas; }
}
