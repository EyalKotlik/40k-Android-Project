import pandas


def main():
    print("Hello World")
    datasheets = pandas.read_csv("Datasheets.csv", sep='|')
    #datasheets.sort_values(datasheets.columns[0], axis=0, inplace=True)
    #datasheets.to_csv("Datasheets.csv",sep="|")
    line_count = 0
    print(datasheets)


if __name__ == "__main__":
    main()
