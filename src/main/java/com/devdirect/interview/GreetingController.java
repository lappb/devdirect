package com.devdirect.interview;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
public class GreetingController {
    @PostMapping(path = "/test1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<LinkedHashMap<String, Object>> test1(@Nullable @RequestParam String[] logs) {
        LinkedHashMap<String, Object> result = new LinkedHashMap();
        for (int i = 0; i < logs.length; i++) {
            String api = logs[i];
            String [] parts = api.split("/");

            // wrong format of path
            if (parts.length < 3) {
                continue;
            }

            // init new project
            if (!result.containsKey(parts[0])) {
                LinkedHashMap<String, Object> child = new LinkedHashMap();
                LinkedHashMap<String, Object> child1 = new LinkedHashMap();

                child.put("_count", 1);
                child1.put("_count", 1);
                child1.put(parts[2], 1);
                child.put(parts[1], child1);
                result.put(parts[0], child);

                continue;
            }

            // handle for existed project
            LinkedHashMap<String, Object> child = (LinkedHashMap)result.get(parts[0]);
            child.replace("_count", Integer.valueOf(child.get("_count").toString()) + 1);
            LinkedHashMap<String, Object> child1 = new LinkedHashMap<>();

            // init new theme
            if (!child.containsKey(parts[1])) {
                child1.put("_count", 1);
                child1.put(parts[2], 1);
                child.put(parts[1], child1);
                continue;
            }

            // handle for existed theme
            child1 = (LinkedHashMap)child.get(parts[1]);
            child1.replace("_count", Integer.valueOf(child1.get("_count").toString()) + 1);

            // init new method
            if (!child1.containsKey(parts[2])) {
                child1.put(parts[2], 1);
                continue;
            }

            // handle for existed method
            child1.replace(parts[2], Integer.valueOf(child1.get(parts[2]).toString()) + 1);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/test2", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<int[][]> test2(@Nullable @RequestParam Boolean[][] input) {
        int row = input.length;
        int col = input[0].length;
        int[][] result = new int[row][col];
        for (int i = 0; i < row; i++ ) {
            for (int j = 0; j < col; j++) {
                if (input[i][j] == true) {
                    result[i][j] = -1;
                    continue;
                }
                int minesAround = 0;
                for(int k = i - 1; k <= i + 1; k++) {
                    if (k < 0 || k > row - 1) {
                        continue;
                    }
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (l < 0 || l > col - 1) {
                            continue;
                        }
                        if (input[k][l] == true) {
                            minesAround += 1;
                        }
                    }
                    result[i][j] = minesAround;
                }
            }
        }

        return ResponseEntity.ok(result);
    }
}
