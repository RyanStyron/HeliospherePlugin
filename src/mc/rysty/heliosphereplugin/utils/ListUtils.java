package mc.rysty.heliosphereplugin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

    public static String fromList(List<String> list, boolean useAnd, boolean formatDuplicates) {
        String string = "";

        if (formatDuplicates) {
            Map<String, Integer> duplicates = new HashMap<>();
            List<String> formatted = new ArrayList<>();

            for (String entry : list)
                duplicates.put(entry, duplicates.getOrDefault(entry, 0) + 1);

            for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
                int amount = entry.getValue();

                formatted.add(entry.getKey() + (amount > 1 ? " x" + amount : ""));
            }

            list = formatted;
        }

        for (int i = 0; i < list.size(); i++) {
            String entry = list.get(i);

            if (i == 0)
                string = entry;
            else if (list.size() == i + 1 && useAnd) {
                if (list.size() == 2)
                    string += " and " + entry;
                else
                    string += ", and " + entry;
            } else
                string += ", " + entry;
        }

        if (string.isEmpty())
            return "None";

        return string;
    }
}
