package com.example.diabetes.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diabetes.R;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTestFragment extends Fragment {

    private EditText mEtPreg;
    private EditText mEtPlas;
    private EditText mEtPress;
    private EditText mEtThick;
    private EditText mEtIns;
    private EditText mEtIndex;
    private EditText mEtFunc;
    private EditText mEtAge;

    private Button mbtSend;
    private Button mBtReset;

    J48 tree = null;

    public NewTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_test, container, false);

        mEtPreg = (EditText) view.findViewById(R.id.etPreg);
        mEtPlas = (EditText) view.findViewById(R.id.etPlas);
        mEtPress = (EditText) view.findViewById(R.id.etPress);
        mEtThick = (EditText) view.findViewById(R.id.etThick);
        mEtIns = (EditText) view.findViewById(R.id.etIns);
        mEtIndex = (EditText) view.findViewById(R.id.etIndex);
        mEtFunc = (EditText) view.findViewById(R.id.etFunc);
        mEtAge = (EditText) view.findViewById(R.id.etAge);

        mbtSend = (Button) view.findViewById(R.id.btSend);
        mBtReset = (Button) view.findViewById(R.id.btReset);

        tree = MainActivity.tree;

        mbtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(mEtPreg.getText().toString(), mEtPlas.getText().toString(), mEtPress.getText().toString(),
                        mEtThick.getText().toString(), mEtIns.getText().toString(), mEtIndex.getText().toString(),
                        mEtFunc.getText().toString(), mEtAge.getText().toString()))
                {
                    Toast.makeText(getActivity(), "Veuillez remplir tous les champs !", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        double preg = Double.parseDouble(mEtPreg.getText().toString());
                        double plas = Double.parseDouble(mEtPlas.getText().toString());
                        double press = Double.parseDouble(mEtPress.getText().toString());
                        double thick = Double.parseDouble(mEtThick.getText().toString());
                        double ins = Double.parseDouble(mEtIns.getText().toString());
                        double index = Double.parseDouble(mEtIndex.getText().toString());
                        double func = Double.parseDouble(mEtFunc.getText().toString());
                        double age = Double.parseDouble(mEtAge.getText().toString());

                        double instanceClass = newInstance(preg, plas, press, thick, ins, index, func, age, tree);
                        if (instanceClass == 0) {
                            Toast.makeText(getActivity(), "Le test est négatif !", Toast.LENGTH_LONG).show();
                        } else {
                            if (instanceClass == 1) {
                                Toast.makeText(getActivity(), "Le test est positif", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Le test est erroné, veuillez réessayer", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getActivity(), "Valeur érronée !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mBtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPreg.setText("");
                mEtPlas.setText("");
                mEtPress.setText("");
                mEtThick.setText("");
                mEtIns.setText("");
                mEtIndex.setText("");
                mEtFunc.setText("");
                mEtAge.setText("");
            }
        });



        return view;
    }


    public double newInstance(double preg, double plas, double press,
                            double thick, double ins, double index,
                            double func, double age, J48 tree)
    {
        Attribute attrPreg = new Attribute("preg");
        Attribute attrPlas = new Attribute("plas");
        Attribute attrPress = new Attribute("pres");
        Attribute attrThick = new Attribute("skin");
        Attribute attrIns = new Attribute("insu");
        Attribute attrIndex = new Attribute("mass");
        Attribute attFunc = new Attribute("pedi");
        Attribute attrAge = new Attribute("age");

        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement("tested_positive");
        fvClassVal.addElement("tested_negative");
        Attribute attrClass = new Attribute("class", fvClassVal);

        FastVector fvWekaAttributes = new FastVector(9);
        fvWekaAttributes.addElement(attrPreg);
        fvWekaAttributes.addElement(attrPlas);
        fvWekaAttributes.addElement(attrPress);
        fvWekaAttributes.addElement(attrThick);
        fvWekaAttributes.addElement(attrIns);
        fvWekaAttributes.addElement(attrIndex);
        fvWekaAttributes.addElement(attFunc);
        fvWekaAttributes.addElement(attrAge);
        fvWekaAttributes.addElement(attrClass);

        Instances dataset = new Instances("abcd", fvWekaAttributes, 0);

        double [] attrValues = new double[dataset.numAttributes()];
        attrValues[0] = preg;
        attrValues[1] = plas;
        attrValues[2] = press;
        attrValues[3] = thick;
        attrValues[4] = ins;
        attrValues[5] = index;
        attrValues[6] = func;
        attrValues[7] = age;

        Instance instance = new Instance(1.0, attrValues);
        dataset.add(instance);

        //Préciser la position de l'attribut class
        dataset.setClassIndex(dataset.numAttributes() - 1);
        try {
            //dataset.instance(0) parce qu'on va tester une seule nouvelle instance
            return tree.classifyInstance(dataset.instance(0));

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    return -1;
    }

    public boolean isEmptyField(String preg, String plas, String press,
                               String thick, String ins, String index,
                               String func, String age)
    {
        if(preg.equals("") || plas.equals("") || press.equals("") || thick.equals("") ||
                ins.equals("") || index.equals("") || func.equals("") || age.equals(""))
        {
            return true;
        }
        return false;
    }

}
