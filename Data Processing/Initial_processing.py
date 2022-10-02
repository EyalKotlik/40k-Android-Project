import pandas


def main():
    print("Hello World")
    datasheets = open("datasheets.csv")
    csv_reader = pandas.reader(datasheets, delimiter="|")
    line_count = 0
    for row in csv_reader:
        if line_count == 0:
            print(f'column names are {", ".join(row)}')
            line_count += 1
        else:
            print(f'ID: {row[0]}, Name: {row[1]}, Role: {row[5]}')
            line_count += 1


if __name__ == "__main__":
    main()
