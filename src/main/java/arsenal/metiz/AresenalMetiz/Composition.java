package arsenal.metiz.AresenalMetiz;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Composition {

    private static final int PIPE_PLOTNOST = 7850;
    private static final int PROV_PLOTNOST = 7900;
    private static final String[] PARAMETERS = {"диаметер трубы", "толщину стенок трубы", "С в трубе", "Si в трубе",
            "Mn в трубе", "Cr в трубе", "Ni в трубе", "Cu в трубе", "С в проволоке", "Si в проволоке", "Mn в проволоке",
            "Cr в проволоке", "Ni в проволоке", "Cu в проволоке"};
    private static final String[] CHEMICALS = {"C", "Si", "Mn", "Cr", "Ni", "Cu"};

    public static Map<String, Double> calculate(String[] input) throws IOException {
        Map<String, Double> inputs = getInputs(input);
        return calculateValues(inputs);
    }

    private static Map<String, Double> calculateValues(Map<String, Double> inputs) {
        Map<String, Double> calculations = new HashMap<>();
        double provVolume = Math.PI * Math.pow(inputs.get(PARAMETERS[0]) - inputs.get(PARAMETERS[1]), 2);
        double pipeVolume = Math.PI * Math.pow(inputs.get(PARAMETERS[0]), 2) - provVolume;
        double provMass = PROV_PLOTNOST * provVolume;
        double pipeMass = PIPE_PLOTNOST * pipeVolume;
        double massSum = provMass + pipeMass;

        int element = 2;
        int dif = CHEMICALS.length;
        for (String elementCh : CHEMICALS) {
            calculations.put(elementCh, formatNumber((pipeMass * inputs.get(PARAMETERS[element]) + provMass *
                    inputs.get(PARAMETERS[element + dif])) / massSum));
            element++;
        }
        return calculations;
    }

    private static Map<String, Double> getInputs(String[] input) throws IOException {
        Map<String, Double> inputs = new HashMap<>();
        int i = 0;
        for (String component : PARAMETERS) {
            double num = Double.parseDouble(input[i]);
            inputs.put(component, formatNumber(num));
            i++;
        }
        return inputs;

    }

    public static double formatNumber(double number) {
        double result = Math.ceil(number * 100) / 100;
        return result;
    }
}
