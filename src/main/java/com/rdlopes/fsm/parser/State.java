package com.rdlopes.fsm.parser;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BiFunction;

public interface State {
    boolean process(Context context);

    enum States implements State, BiFunction<Scanner, Context, Boolean> {
        READING_LAWN_DIMENSIONS {
            @Override
            public Boolean apply(Scanner scanner, Context context) {
                int x = -1;
                if (scanner.hasNextInt()) {
                    x = scanner.nextInt();
                }

                int y = -1;
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt();
                }

                if (x > 0 && y > 0) {
                    context.setState(READING_MOWER_COORDINATES);
                    return context.setLawn(x + 1, y + 1);
                }

                return false;
            }
        },
        READING_MOWER_COORDINATES {
            @Override
            public Boolean apply(Scanner scanner, Context context) {
                int x = -1;
                if (scanner.hasNextInt()) {
                    x = scanner.nextInt();
                }

                int y = -1;
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt();
                }

                String orientation = null;
                if (scanner.hasNext("[NEWS]")) {
                    orientation = scanner.next();
                }

                if (x > 0 && y > 0 && orientation != null) {
                    context.setState(READING_MOWER_INSTRUCTIONS);
                    return context.setMowerCoordinates(x, y, orientation);
                }

                return false;
            }
        },
        READING_MOWER_INSTRUCTIONS {
            @Override
            public Boolean apply(Scanner scanner, Context context) {
                String instructions = null;
                if (scanner.hasNext("[LRF]+")) {
                    instructions = scanner.next();
                }

                if (instructions != null) {
                    context.setState(context.hasContent() ? READING_MOWER_COORDINATES : END);
                    return context.setMowerInstructions(Arrays.asList(instructions.split("(?!^)")));
                }

                return false;
            }
        },
        END {
            @Override
            public Boolean apply(Scanner scanner, Context context) {
                return false;
            }
        };

        private final LineReaderStateHelper helper;

        States() {
            this.helper = new LineReaderStateHelper(this);
        }

        @Override
        public boolean process(Context context) {
            return helper.process(context);
        }

        private static class LineReaderStateHelper implements State {
            private final BiFunction<Scanner, Context, Boolean> extractor;

            private LineReaderStateHelper(BiFunction<Scanner, Context, Boolean> extractor) {
                this.extractor = extractor;
            }

            @Override
            public boolean process(Context context) {
                if (!context.hasContent()) {
                    return false;
                }

                try (Scanner scanner = new Scanner(context.nextContent())) {
                    return extractor.apply(scanner, context);
                }
            }
        }
    }
}
