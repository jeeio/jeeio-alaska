package io.jee.alaska.data.hibernate;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

public class PrefixPhysicalNamingStrategy implements PhysicalNamingStrategy {
	
	private String tablePrefix;
	
	public PrefixPhysicalNamingStrategy() throws IOException {
		ClassPathResource cpr = new ClassPathResource("application.properties");
		if(cpr.exists()){
			Properties properties = new Properties();
			properties.load(cpr.getInputStream());
			tablePrefix = (String) properties.getProperty("alaska.orm.prefix");
		}
		cpr = new ClassPathResource("application.yml");
		if(cpr.exists()&&!StringUtils.hasText(tablePrefix)){
			Yaml yaml = new Yaml();
			@SuppressWarnings("unchecked")
			Map<String, Object> result= (Map<String, Object>) yaml.load(cpr.getInputStream());
			if(result!=null){
				@SuppressWarnings("unchecked")
				Map<String, Object> alaska = (Map<String, Object>) result.get("alaska");
				if(alaska!=null){
					@SuppressWarnings("unchecked")
					Map<String, String> orm = (Map<String, String>) alaska.get("orm");
					if(orm!=null){
						tablePrefix = orm.get("prefix");
					}
				}
			}
		}
	
		if(!StringUtils.hasText(tablePrefix)){
			tablePrefix = "ala_";
		}
	}
	
	@Override
	public Identifier toPhysicalCatalogName(Identifier name,
			JdbcEnvironment jdbcEnvironment) {
		return apply(name, jdbcEnvironment);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name,
			JdbcEnvironment jdbcEnvironment) {
		return apply(name, jdbcEnvironment);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name,
			JdbcEnvironment jdbcEnvironment) {
		String text = tablePrefix + name.getText();
		return apply(Identifier.toIdentifier(text), jdbcEnvironment);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name,
			JdbcEnvironment jdbcEnvironment) {
		return apply(name, jdbcEnvironment);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name,
			JdbcEnvironment jdbcEnvironment) {
		return apply(name, jdbcEnvironment);
	}

	private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
		if (name == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));
		for (int i = 1; i < builder.length() - 1; i++) {
			if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
					builder.charAt(i + 1))) {
				builder.insert(i++, '_');
			}
		}
		return getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
	}

	/**
	 * Get an the identifier for the specified details. By default this method will return
	 * an identifier with the name adapted based on the result of
	 * {@link #isCaseInsensitive(JdbcEnvironment)}
	 * @param name the name of the identifier
	 * @param quoted if the identifier is quoted
	 * @param jdbcEnvironment the JDBC environment
	 * @return an identifier instance
	 */
	protected Identifier getIdentifier(String name, boolean quoted,
			JdbcEnvironment jdbcEnvironment) {
		if (isCaseInsensitive(jdbcEnvironment)) {
			name = name.toLowerCase(Locale.ROOT);
		}
		return new Identifier(name, quoted);
	}

	/**
	 * Specify whether the database is case sensitive.
	 * @param jdbcEnvironment the JDBC environment which can be used to determine case
	 * @return true if the database is case insensitive sensitivity
	 */
	protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
		return true;
	}

	private boolean isUnderscoreRequired(char before, char current, char after) {
		return Character.isLowerCase(before) && Character.isUpperCase(current)
				&& Character.isLowerCase(after);
	}

}
