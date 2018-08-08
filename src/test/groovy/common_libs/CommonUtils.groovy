package common_libs

import org.yaml.snakeyaml.Yaml


class CommonUtils {

    def mysql_url= [:]
    public static envParams = null
    def env_tag = 'tlmqe'


    def read_properties()
    {
        envParams = new Yaml().load(new FileReader(System.getProperty("user.dir") + '/src/test/groovy/config/tlm_env.yml'))[env_tag]
        if (envParams==null)
        {
            throw new Exception("No ENV defined for the env_tag="+ env_tag  + "under config/tlm_env.yml, please check and retry")
        }
        return envParams
    }

    def raise_unknown_db_exception(db_config)
    {
        throw new Exception("Database name not found in the Yaml file, please check and retry")
    }

     

    def add_mysql_url(db_config)
    {
        if(db_config==null)
        {
            return raise_unknown_db_exception(db_config)
        }
        else
        {
            mysql_url['url'] = "jdbc:mysql://${db_config['host']}:${db_config['port']}/${db_config["service"]}"
            return (db_config + mysql_url)
        }
    }

}