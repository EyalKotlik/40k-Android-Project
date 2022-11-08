import pandas
import re


def main():

    # this part of the code is for processing the data from datasheets, primarly by removing unnessecary columns
    print("Processing data from Datasheets.csv...")
    data = pandas.read_csv("Raw Data/Datasheets.csv", sep='|')
    data.sort_values(data.columns[0], axis=0, inplace=True)
    data.drop('link', inplace=True, axis=1)
    data.drop('open_play_only', inplace=True, axis=1)
    data.drop('crusade_only', inplace=True, axis=1)
    data.drop('virtual', inplace=True, axis=1)
    data.drop('power_points', inplace=True, axis=1)
    data.drop('priest', inplace=True, axis=1)
    data.drop('psyker', inplace=True, axis=1)
    data.drop('transport', inplace=True, axis=1)
    data.drop('Unnamed: 16', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.lower()
    data.to_csv("Processed Data/Datasheets.csv", sep="|", index=False)

    print("Processing data from Datasheets_keywords.csv...")
    data = pandas.read_csv("Raw Data/Datasheets_keywords.csv", sep='|')
    data.drop('Unnamed: 4', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.lower()
    data.to_csv("Processed Data/Datasheets_keywords.csv",
                sep="|", index=False)

    print("Processing data from Datasheets_models.csv...")
    data = pandas.read_csv("Raw Data/Datasheets_models.csv", sep='|')
    data.drop('Unnamed: 18', inplace=True, axis=1)
    data.drop('base_size', inplace=True, axis=1)
    data.drop('base_size_descr', inplace=True, axis=1)
    data.drop('M', inplace=True, axis=1)
    data.drop('Ld', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.replace('[+"]', '')
            data[col] = data[col].str.lower()
    data.to_csv("Processed Data/Datasheets_models.csv", sep="|", index=False)

    print("Processing data from Datasheets_weapons.csv...")
    data = pandas.read_csv("Raw Data/Datasheets_wargear.csv", sep='|')
    data.drop('Unnamed: 7', inplace=True, axis=1)
    data.drop('is_index_wargear', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.lower()
    data.to_csv("Processed Data/Datasheets_wargear.csv",
                sep="|", index=False)

    print("Processing data from Wargear.csv...")
    data = pandas.read_csv("Raw Data/Wargear.csv", sep='|')
    data.drop('Unnamed: 9', inplace=True, axis=1)
    data.drop('legend', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.lower()
    data.drop('source_id', inplace=True, axis=1)
    data.to_csv("Processed Data/Wargear.csv",
                sep="|", index=False)

    print("Processing data from Wargear_list.csv...")
    data = pandas.read_csv("Raw Data/Wargear_list.csv", sep='|')
    data.drop('Unnamed: 9', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.replace('["]', '')
            data[col] = data[col].str.lower()
    data.to_csv("Processed Data/Wargear_list.csv",
                sep="|", index=False)

    print("Data Processing Complete")


if __name__ == "__main__":
    main()
