ALTER TABLE operators
    ADD COLUMN contract_start_date DATE NULL,
    ADD COLUMN contract_end_date DATE NULL,
    ADD COLUMN founded_date DATE NULL;

ALTER TABLE operators_aud
    ADD COLUMN contract_start_date DATE NULL,
    ADD COLUMN contract_end_date DATE NULL,
    ADD COLUMN founded_date DATE NULL;

UPDATE operators o
JOIN operator_configurations oc ON oc.operator_id = o.operator_id
SET o.contract_start_date = oc.contract_start_date,
    o.contract_end_date = oc.contract_end_date,
    o.founded_date = oc.founded_date
WHERE oc.contract_start_date IS NOT NULL
   OR oc.contract_end_date IS NOT NULL
   OR oc.founded_date IS NOT NULL;

UPDATE operators_aud oa
JOIN operator_configurations_aud oca
  ON oca.operator_id = oa.operator_id
 AND oca.rev = oa.rev
SET oa.contract_start_date = oca.contract_start_date,
    oa.contract_end_date = oca.contract_end_date,
    oa.founded_date = oca.founded_date
WHERE oca.contract_start_date IS NOT NULL
   OR oca.contract_end_date IS NOT NULL
   OR oca.founded_date IS NOT NULL;

ALTER TABLE operator_configurations
    DROP COLUMN contract_start_date,
    DROP COLUMN contract_end_date,
    DROP COLUMN founded_date;

ALTER TABLE operator_configurations_aud
    DROP COLUMN contract_start_date,
    DROP COLUMN contract_end_date,
    DROP COLUMN founded_date;
