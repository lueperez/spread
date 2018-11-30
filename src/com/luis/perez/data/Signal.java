package com.luis.perez.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Luis Eduardo Perez
 */
public class Signal {

    private List<Data> dataList = new ArrayList<>();
    private String[] combinedSignal;
    private Integer spreadLength = 8;

    private Signal() {
    }

    public static Signal builder() {
        return new Signal();
    }

    public void spread() {
        Scanner scanner = new Scanner(System.in);
        String next;

        int index = 1;
        do {
            Data data = new Data();
            print(String.format(Message.insert_data, index));

            index++;
            data.setData(scanner.nextLine().split(""));
            data.setIndex(index);
            print(Message.insert_spread);
            int pepe = Integer.parseInt(scanner.nextLine().substring(0, spreadLength), 2);
            data.setSpreadingCode(pepe);
            String binaryString = Integer.toBinaryString(pepe);
            int binaryBits = Integer.valueOf(binaryString);
            String pepeFinal = String.format("%0".concat(spreadLength.toString()).concat("d"), binaryBits);

            if (pepeFinal.length() > spreadLength) {
                print(String.format(Message.spread_length_error, spreadLength));
                System.exit(0);
            }

            dataList.add(data);
            print(Message.end_or_continue);
            next = scanner.nextLine();
        } while (!next.equalsIgnoreCase("x"));

        dataList.forEach(data -> data.setSpreadingMessage(spreadData(data.getData(), data.getSpreadingCode(), data.getIndex())));
        combineSignals(dataList);
        print(String.format(Message.combined_signals, Arrays.toString(combinedSignal)));

        dataList.forEach(data -> {
            String[] getOriginDeltaArray = getOriginalData(data);
            if (Arrays.equals(data.getData(), getOriginDeltaArray))
                print(String.format(Message.data_recovered, Arrays.toString(data.getData())));
            else
                print(String.format(Message.no_data_recovered, Arrays.toString(data.getData())));
        });
    }

    private String[] spreadData(String[] userDataString, int spreadingCode, int index) {
        final StringBuilder[] userData = {new StringBuilder()};
        List<Integer> chips = new ArrayList<>();

        Arrays.asList(userDataString)
                .forEach(anUserDataString -> {
                    for (int i = 0; i < spreadLength; i++) {
                        userData[0].append(anUserDataString);
                    }
                    int newBit = Integer.parseInt(userData[0].toString(), 2);
                    chips.add(newBit ^ spreadingCode);
                    userData[0] = new StringBuilder();

                });

        StringBuilder binaryString = new StringBuilder();

        chips.forEach(chip -> {
            String binary = Integer.toBinaryString(chip);
            int binaryBits = Integer.valueOf(binary);
            binaryString.append(String.format("%0".concat(spreadLength.toString()).concat("d"), binaryBits));
        });

        print(String.format(Message.spread_msg, index - 1, binaryString));
        return binaryString.toString().split("");
    }

    private void combineSignals(List<Data> data) {
        combinedSignal = new String[data.get(0).getSpreadingMessage().length];
        for (int i = 0; i < data.size(); i++) {
            if (i + 1 == data.size())
                break;

            String[] spreadArray1;
            String[] spreadArray2;

            if (i == 0) {
                spreadArray1 = Arrays.copyOf(data.get(i)
                        .getSpreadingMessage(), data.get(i)
                        .getSpreadingMessage().length);

                spreadArray2 = Arrays.copyOf(data.get(i + 1)
                        .getSpreadingMessage(), data.get(i + 1)
                        .getSpreadingMessage().length);
            } else {
                spreadArray1 = Arrays.copyOf(data.get(i + 1)
                        .getSpreadingMessage(), data.get(i + 1)
                        .getSpreadingMessage().length);

                spreadArray2 = combinedSignal;
            }

            for (int k = 0; k < spreadArray1.length; k++) {
                int currentBit1 = Integer.parseInt(String
                        .valueOf(spreadArray1[k]));
                int currentBit2 = Integer.parseInt(String
                        .valueOf(spreadArray2[k]));

                if (currentBit1 == 0)
                    currentBit1 = 1;
                else
                    currentBit1 = -1;

                if (i == 0) {
                    if (currentBit2 == 0)
                        currentBit2 = 1;
                    else
                        currentBit2 = -1;
                }

                int sumBits;

                if (i == 0)
                    sumBits = currentBit1 + currentBit2;
                else
                    sumBits = currentBit2 + currentBit1;

                combinedSignal[k] = String.valueOf(sumBits);

            }

        }

    }

    private String[] getOriginalData(Data data) {

        String binaryString = Integer.toBinaryString(data.getSpreadingCode());
        int binaryBits = Integer.valueOf(binaryString);
        String[] userCodeArray = String.format("%0".concat(spreadLength.toString()).concat("d"), binaryBits).split("");
        combinedSignal = data.getData();
        String[] deSpread1 = new String[combinedSignal.length];
        int count = 0;

        for (int i = 0; i < userCodeArray.length; i++) {

            if (count == spreadLength)
                break;

            int currentBit1 = Integer.parseInt(String.valueOf(userCodeArray[i]));

            if (currentBit1 == 0)
                currentBit1 = 1;
            else
                currentBit1 = -1;

            deSpread1[count] = String.valueOf(currentBit1 * Long.valueOf(combinedSignal[count]));

            if (count == spreadLength - 1)
                i = 0;

            count++;

        }

        int sumBits1 = 0;
        int sumBits2 = 0;

        for (int i = 0; i < deSpread1.length; i++) {

            if (i < spreadLength)
                sumBits1 += Integer.valueOf(deSpread1[i]);
            else
                sumBits2 += Integer.valueOf(deSpread1[i]);

        }

        int userCode1 = sumBits1 / spreadLength;
        int userCode2 = sumBits2 / spreadLength;

        userCode1 = 1 - userCode1;
        userCode2 = 1 - userCode2;

        return new String[]{String.valueOf(userCode1), String.valueOf(userCode2)};

    }

    private static void print(String msg) {
        System.out.println(msg);
    }
}
