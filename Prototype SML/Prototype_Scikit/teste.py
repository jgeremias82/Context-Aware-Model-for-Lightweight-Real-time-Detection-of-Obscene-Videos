#!/usr/bin/env python
'''
This example demonstrates a simple use of pycallgraph.
'''
import pycallgraph as pcg


class Banana:

    def eat(self):
        pass


class Person:

    def __init__(self):
        self.no_bananas()

    def no_bananas(self):
        self.bananas = []

    def add_banana(self, banana):
        self.bananas.append(banana)

    def eat_bananas(self):
        [banana.eat() for banana in self.bananas]
        self.no_bananas()


def main():
    graphviz = GraphvizOutput()
    #graphviz.output_file = 'basic.png'
    pcg.output  = 'basic.png'
    with pcg.PyCallGraph(output=pcg):
        person = Person()
        for a in xrange(10):
            person.add_banana(Banana())
        person.eat_bananas()


if __name__ == '__main__':
    main()
