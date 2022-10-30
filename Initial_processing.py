import pandas
import re


def main():

    # this part of the code is for processing the data from datasheets, primarly by removing unnessecary columns
    # TO DO: process unit composition data
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
    data.to_csv("Data Processing/Datasheets.csv", sep="|", index=False)


    # what is "is_other_wargear"?
    data = pandas.read_csv("Raw Data/Datasheets_abilities.csv", sep='|')
    data.sort_values(data.columns[0], axis=0, inplace=True)
    data.drop('is_index_wargear', inplace=True, axis=1)
    data.to_csv("Data Processing/Datasheets_abilities.csv",
                sep="|", index=False)


    data = pandas.read_csv("Raw Data/Datasheets_keywords.csv", sep='|')
    data.drop('Unnamed: 4', inplace=True, axis=1)
    data.to_csv("Data Processing/Datasheets_keywords.csv",
                sep="|", index=False)


    data = pandas.read_csv("Raw Data/Datasheets_models.csv", sep='|')
    data.drop('Unnamed: 18', inplace=True, axis=1)
    data.drop('base_size', inplace=True, axis=1)
    data.drop('base_size_descr', inplace=True, axis=1)
    data.drop('M', inplace=True, axis=1)
    data.drop('Ld', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.replace('[+"]', '')
    data.to_csv("Data Processing/Datasheets_models.csv", sep="|", index=False)


    data = pandas.read_csv("Raw Data/Datasheets_wargear.csv", sep='|')
    data.drop('Unnamed: 7', inplace=True, axis=1)
    data.drop('is_index_wargear', inplace=True, axis=1)
    data.to_csv("Data Processing/Datasheets_wargear.csv",
                sep="|", index=False)


    data = pandas.read_csv("Raw Data/Wargear.csv", sep='|')
    data.drop('Unnamed: 9', inplace=True, axis=1)
    data.drop('legend', inplace=True, axis=1)
    data.drop('source_id', inplace=True, axis=1)
    data.to_csv("Data Processing/Wargear.csv",
                sep="|", index=False)


    # make the abilities readable
    data = pandas.read_csv("Raw Data/Wargear_list.csv", sep='|')
    data.drop('Unnamed: 9', inplace=True, axis=1)
    for col in data.columns:
        if data[col].dtype == object:
            data[col] = data[col].str.replace('["]', '')
    data.to_csv("Data Processing/Wargear_list.csv",
                sep="|", index=False)
    
    data = pandas.read_csv("Raw Data/Abilities.csv", sep='|')
    data.drop('Unnamed: 7', inplace=True, axis=1)
    data.drop('legend', inplace=True, axis=1)
    data.drop('is_other_wargear', inplace=True, axis=1)
    count = 0
    index = 0
    for desc in data["description"]:
        processed_desc = ""
        if re.search("deploy|transport|manifest|psychic|move |morale|repair|explode|embark|test|charge roll|objective secured|regain|set up|declare a charge|leadership|battle-forged",desc.lower()):
            count += 1
            #print(desc)
            data['description'][index] = "irrelevant"
        data['description'][index]  = re.sub("<[^<>]*>","",data['description'][index].lower()) # removes links
        # format varius things
        data['description'][index]  = re.sub("&lt;|&gt;","-key-",data['description'][index]) # marks a keyword
        data['description'][index]  = re.sub("core unit","-cu-",data['description'][index]) # core unit
        data['description'][index]  = re.sub("enemy","-eu-",data['description'][index]) # enemy unit
        data['description'][index]  = re.sub("friendly","-fu-",data['description'][index]) # friendly unit
        data['description'][index]  = re.sub("this model","-ma-",data['description'][index].lower()) # model ability
        data['description'][index]  = re.sub("this unit","-ua-",data['description'][index].lower()) # unit ability
        data['description'][index]  = re.sub("\+ invulnerable save","-isv-",data['description'][index].lower()) # invulnerable save
        data['description'][index]  = re.sub("an unmodified hit roll of","-uhr-",data['description'][index].lower()) # unmodified hit roll
        data['description'][index]  = re.sub("an unmodified wound roll of","-uwr-",data['description'][index].lower()) # unmodified wound roll
        data['description'][index]  = re.sub("subtract 1 from that attack’s hit roll","-s1hr-",data['description'][index].lower()) # subtract 1 from hit roll
        data['description'][index]  = re.sub("subtract 1 from that attack’s wound roll","-s1wr-",data['description'][index].lower()) # subtract 1 from wound roll
        data['description'][index]  = re.sub("each time an attack is made|each time an attack is allocated to a model","-ea-",data['description'][index].lower()) # enemy attack [is made]
        data['description'][index]  = re.sub("each time a melee attack is made against|against melee attacks","-ema-",data['description'][index].lower()) # enemy melee attack [is made]
        data['description'][index]  = re.sub("each time a ranged attack is made against|against ranged attacks","-era-",data['description'][index].lower()) # enemy ranged attack [is made]

        index += 1
    data.to_csv("Data Processing/Abilities.csv",
                sep="|", index=False)
    print(data)
    print(count/index*100,"%")

if __name__ == "__main__":
    main()
