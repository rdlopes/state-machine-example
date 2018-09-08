# State machine example [![Build Status](https://travis-ci.org/rdlopes/state-machine-example.svg?branch=master)](https://travis-ci.org/rdlopes/state-machine-example)

This implementation is an example of using enum-based state machines to parse a file with a formatted content.

## The file format

The file parsed is the description of a lawn having multiple mowers on it, ready to execute instructions.
The format is as follow:

* First line is coordinates for the upper right corner of the lawn, in the form X Y, assuming bottom left corner is at coordinates (0,0).
* Then following lines are always appearing in pairs and describe the mowers as follow:
    
    * A first line to describe mower's position and orientation in the form X Y O, where O can be one of N,E,W,S for NORTH, EAST, WEST, SOUTH orientations.
    * A second line holds instructions for the mower in the form L,R,F meaning respectively LEFT, RIGHT, FORWARD.
    
## The state machine

State machine is composed of a context - the parser - and states for each type of line encountered.

States are defined as enums inside the State interface directly.
