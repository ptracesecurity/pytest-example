import sys

from myapp.app import multiply_by_two, divide_by_two


def main():
    num = int(input("Insert a number: "))
    print("The double of %d is %d" % (num, multiply_by_two(num)))
    print("The half of %d is %d" % (num, divide_by_two(num)))
    sys.exit(0)


if __name__ == "__main__":
    main()
