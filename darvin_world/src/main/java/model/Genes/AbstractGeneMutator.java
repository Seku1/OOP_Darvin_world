package model.Genes;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class AbstractGeneMutator implements GeneMutator {
    protected final int minimumNumberOfMutations;
    protected final int maximumNumberOfMutations;
    protected final int maxValue;
    protected final Random random = new Random();

    protected AbstractGeneMutator(int minimumNumberOfMutations, int maximumNumberOfMutations, int maxValue) {
        if (minimumNumberOfMutations < 0 || maximumNumberOfMutations < minimumNumberOfMutations) {
            throw new IllegalArgumentException("Invalid mutation range.");
        }
        this.minimumNumberOfMutations = minimumNumberOfMutations;
        this.maximumNumberOfMutations = maximumNumberOfMutations;
        this.maxValue = maxValue;
    }

    @Override
    public void mutate(int[] genes) {
        int howManyChanged = random.nextInt(maximumNumberOfMutations - minimumNumberOfMutations + 1) + minimumNumberOfMutations;
        Set<Integer> indicesToChange = selectUniqueIndices(genes.length, howManyChanged);

        for (int index : indicesToChange) {
            mutateGene(index, genes);
        }
    }

    private Set<Integer> selectUniqueIndices(int arrayLength, int count) {
        Set<Integer> indices = new HashSet<>();
        while (indices.size() < count) {
            indices.add(random.nextInt(arrayLength));
        }
        return indices;
    }

    protected abstract void mutateGene(int geneIndex, int[] genes);
}
