# This is a sample Python script.
# https://pqjc.site/api/v1/client/subscribe?token=d2d80673a1b931daafc45391ba8b3695
# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.





# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    base = 1000000
    res = 0
    while base >= 0:
        base += base * 0.02
        res += 1
        # base -= 36000
        base -= 12 * 2500
    print(res)
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
