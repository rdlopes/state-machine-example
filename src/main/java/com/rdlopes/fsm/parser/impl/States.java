package com.rdlopes.fsm.parser.impl;

import com.rdlopes.fsm.domain.Mower;
import com.rdlopes.fsm.parser.fsm.Context;
import com.rdlopes.fsm.parser.fsm.State;
import com.rdlopes.fsm.parser.fsm.StateProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;

@Slf4j
enum States implements State {
    READING_FILE {
        private File configurationFile = null;

        @Override
        public boolean canProcess(Context context) {
            try {
                this.configurationFile = context.getInputResource().getFile();
                return isReadable(configurationFile);

            } catch (IOException e) {
                log.error(format("Accessing file {0} raised exception", configurationFile), e);
                return false;
            }
        }

        @Override
        public State process(Context context) throws StateProcessingException {
            try {
                Files.readAllLines(configurationFile.toPath(), context.getInputCharset())
                     .forEach(context::addContentLine);
                return READING_LAWN_DIMENSIONS;

            } catch (IOException e) {
                throw new StateProcessingException(format("Reading file {0} raised exception", configurationFile), e);
            }
        }
    },
    READING_LAWN_DIMENSIONS {
        @Override
        public boolean canProcess(Context context) {
            return context.hasMoreLines();
        }

        @Override
        public State process(Context context) throws StateProcessingException {
            String contentLine = context.getNextLine();
            try (Scanner scanner = new Scanner(contentLine)) {
                int x;
                if (scanner.hasNextInt()) {
                    x = scanner.nextInt();

                } else {
                    throw new StateProcessingException("Could not read x value");
                }

                int y;
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt();
                } else {
                    throw new StateProcessingException("Could not read y value");
                }

                if (x >= 0 && y >= 0) {
                    context.setLawnDimension(x + 1, y + 1);
                    return READING_MOWER_COORDINATES;

                } else {
                    throw new StateProcessingException(format("Cannot accept values x:{0}, y:{1}", x, y));
                }

            } catch (Exception e) {
                throw new StateProcessingException(format("Failed scanning line {0}", contentLine), e);
            }
        }
    },
    READING_MOWER_COORDINATES {
        @Override
        public boolean canProcess(Context context) {
            return context.hasMoreLines();
        }

        @Override
        public State process(Context context) throws StateProcessingException {
            String contentLine = context.getNextLine();
            try (Scanner scanner = new Scanner(contentLine)) {
                int x;
                if (scanner.hasNextInt()) {
                    x = scanner.nextInt();

                } else {
                    throw new StateProcessingException("Could not read x value");
                }

                int y;
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt();
                } else {
                    throw new StateProcessingException("Could not read y value");
                }

                String orientationCode;
                if (scanner.hasNext("[NEWS]")) {
                    orientationCode = scanner.next();

                } else {
                    throw new StateProcessingException("Could not read orientation value");
                }

                if (x >= 0 && y >= 0 && orientationCode != null) {
                    Mower.Orientation orientation = Mower.Orientation.valueOf(orientationCode);
                    context.setMowerCoordinates(x, y, orientation);
                    return READING_MOWER_INSTRUCTIONS;

                } else {
                    throw new StateProcessingException(format("Cannot accept values x:{0}, y:{1}, orientationCode:{2}", x, y, orientationCode));
                }

            } catch (Exception e) {
                throw new StateProcessingException(format("Failed scanning line {0}", contentLine), e);
            }
        }
    },
    READING_MOWER_INSTRUCTIONS {
        @Override
        public boolean canProcess(Context context) {
            return context.hasMoreLines();
        }

        @Override
        public State process(Context context) throws StateProcessingException {
            String contentLine = context.getNextLine();
            try (Scanner scanner = new Scanner(contentLine)) {
                String instructionCodes;
                if (scanner.hasNext("[LRF]+")) {
                    instructionCodes = scanner.next();

                } else {
                    throw new StateProcessingException("Could not read instruction value");
                }

                if (instructionCodes != null) {
                    List<Mower.Instruction> instructions = Arrays.stream(instructionCodes.split("(?!^)"))
                                                                 .map(Mower.Instruction::valueOf)
                                                                 .collect(toList());

                    context.setMowerInstructions(instructions);
                    return context.hasMoreLines() ? READING_MOWER_COORDINATES : END;

                } else {
                    throw new StateProcessingException(format("Cannot accept values instructionCodes:{0}", instructionCodes));
                }

            } catch (Exception e) {
                throw new StateProcessingException(format("Failed scanning line {0}", contentLine), e);
            }
        }
    },
    END {
        @Override
        public boolean canProcess(Context context) {
            return false;
        }

        @Override
        public State process(Context context) {
            return this;
        }
    };

    boolean isReadable(File file) {
        return file != null &&
               file.exists() &&
               file.isFile() &&
               file.canRead();
    }
}
