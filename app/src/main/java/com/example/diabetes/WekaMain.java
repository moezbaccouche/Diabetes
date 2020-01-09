package com.example.diabetes;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class WekaMain {

    public static J48 loadDecisionTree(String path)
    {

        ConverterUtils.DataSource source;
        try {
            source = new ConverterUtils.DataSource(path);
            Instances data = source.getDataSet();
            Instances structure = source.getStructure();
            data.setClassIndex(structure.numAttributes() - 1);
            J48 tree = new J48();
            tree.buildClassifier(data);

            Classifier c1 = new J48();
            Evaluation eval_roc = new Evaluation(data);
            eval_roc.crossValidateModel(c1, data, 10, new Debug.Random(1), new Object[]{});
            //System.out.println("Taux d'erreurs par VC : " + eval_roc.errorRate());
            return tree;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args)
    {
        System.out.println(loadDecisionTree("app/files/diabetes_arff.arff"));
    }
}
