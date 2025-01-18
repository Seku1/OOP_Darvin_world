package model.Genes;

public class RegularMutation extends AbstractGeneMutator{
    public RegularMutation(int minimumNumberOfMutations, int maximumNumberOfMutations, int maxValue) {
        super(minimumNumberOfMutations, maximumNumberOfMutations, maxValue);
    }

    @Override
    protected void mutateGene(int geneIndex, int[] genes) {
        genes[geneIndex] = random.nextInt(maxValue + 1);
    }
}
